package cart.application.service;

import cart.application.dto.order.OrderCartItemProductRequest;
import cart.application.dto.order.OrderCartItemsRequest;
import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberCouponRepository;
import cart.application.repository.OrderRepository;
import cart.domain.Member;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import cart.domain.coupon.MemberCoupon;
import cart.domain.order.Order;
import cart.exception.StoreException;
import cart.exception.notfound.MemberCouponNotFoundException;
import cart.exception.notfound.OrderNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final OrderRepository orderRepository;

    public OrderService(final CartItemRepository cartItemRepository,
            final MemberCouponRepository memberCouponRepository,
            final OrderRepository orderRepository) {
        this.cartItemRepository = cartItemRepository;
        this.memberCouponRepository = memberCouponRepository;
        this.orderRepository = orderRepository;
    }

    public long orderCartItems(final Member member, final OrderCartItemsRequest request) {
        Map<Long, OrderCartItemProductRequest> cartProducts = request.getProducts().stream()
                .collect(Collectors.toMap(OrderCartItemProductRequest::getCartItemId, cartItem -> cartItem));

        CartItems cartItems = cartItemRepository.findByIds(new ArrayList<>(cartProducts.keySet()));
        cartItems.checkOwner(member);
        validateCartItems(cartProducts, cartItems);

        MemberCoupon memberCoupon = memberCouponRepository.findById(request.getCouponId())
                .orElseThrow(MemberCouponNotFoundException::new);
        memberCoupon.checkOwner(member);

        Order order = Order.of(member, cartItems, memberCoupon);

        cartItemRepository.deleteAll(cartItems);
        memberCouponRepository.use(memberCoupon);
        return orderRepository.order(order);
    }

    private void validateCartItems(final Map<Long, OrderCartItemProductRequest> cartProductRequests,
            final CartItems cartItems) {
        for (final CartItem cartItem : cartItems.getCartItems()) {
            OrderCartItemProductRequest cartProductRequest = cartProductRequests.get(cartItem.getId());
            if (cartItem.getQuantity() != cartProductRequest.getQuantity()) {
                throw new StoreException("주문 수량을 확인해주세요.");
            }
            if (cartItem.getProduct().getPrice() != cartProductRequest.getPrice()) {
                throw new StoreException("가격을 확인해주세요.");
            }
        }
    }

    @Transactional(readOnly = true)
    public Order getOrderById(final Member member, final long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.checkOwner(member);

        return order;
    }
}

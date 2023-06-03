package cart.application.service;

import static cart.exception.badrequest.BadRequestErrorType.CART_ITEM_PRICE_INCORRECT;
import static cart.exception.badrequest.BadRequestErrorType.CART_ITEM_QUANTITY_INCORRECT;
import static cart.exception.noexist.NoExistErrorType.COUPON_NO_EXIST;
import static cart.exception.noexist.NoExistErrorType.ORDER_NO_EXIST;

import cart.application.dto.order.CreateOrderByCartItemsRequest;
import cart.application.dto.order.FindOrderDetailResponse;
import cart.application.dto.order.FindOrdersResponse;
import cart.application.dto.order.OrderProductRequest;
import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberCouponRepository;
import cart.application.repository.OrderRepository;
import cart.domain.Member;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import cart.domain.coupon.MemberCoupon;
import cart.domain.order.Order;
import cart.exception.badrequest.BadRequestException;
import cart.exception.noexist.NoExistException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public long orderCartItems(final Member member, final CreateOrderByCartItemsRequest request) {
        Map<Long, OrderProductRequest> cartProductRequests = request.getProducts().stream()
                .collect(Collectors.toMap(OrderProductRequest::getCartItemId, cartItem -> cartItem));

        CartItems cartItems = cartItemRepository.findByIds(new ArrayList<>(cartProductRequests.keySet()));
        validateCartItems(cartProductRequests, cartItems);

        Long memberCouponId = request.getCouponId();
        if (Objects.isNull(memberCouponId)) {
            Order order = Order.of(member, cartItems, MemberCoupon.none(member));
            cartItemRepository.deleteAll(cartItems);
            return orderRepository.order(order);
        }

        MemberCoupon memberCoupon = memberCouponRepository.findById(memberCouponId)
                .orElseThrow(() -> new NoExistException(COUPON_NO_EXIST));
        Order order = Order.of(member, cartItems, memberCoupon);

        cartItemRepository.deleteAll(cartItems);
        memberCouponRepository.use(memberCoupon);
        return orderRepository.order(order);
    }

    private void validateCartItems(final Map<Long, OrderProductRequest> cartProductRequests,
            final CartItems cartItems) {
        for (final CartItem cartItem : cartItems.getCartItems()) {
            OrderProductRequest cartProductRequest = cartProductRequests.get(cartItem.getId());
            if (cartItem.getQuantity() != cartProductRequest.getQuantity()) {
                throw new BadRequestException(CART_ITEM_QUANTITY_INCORRECT);
            }
            if (cartItem.getProduct().getPrice() != cartProductRequest.getPrice()) {
                throw new BadRequestException(CART_ITEM_PRICE_INCORRECT);
            }
        }
    }

    @Transactional(readOnly = true)
    public FindOrderDetailResponse findOrderById(final Member member, final long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoExistException(ORDER_NO_EXIST));
        order.checkOwner(member);

        return FindOrderDetailResponse.from(order);
    }

    @Transactional(readOnly = true)
    public FindOrdersResponse findOrders(final Member member) {
        List<Order> orders = orderRepository.findByMember(member);

        return FindOrdersResponse.from(orders);
    }
}

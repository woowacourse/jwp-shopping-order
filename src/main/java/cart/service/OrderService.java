package cart.service;

import static java.util.stream.Collectors.toList;

import cart.domain.cart.CartItem;
import cart.domain.cart.Product;
import cart.domain.order.MemberCoupon;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrderSaveRequest;
import cart.exception.order.OrderNotFoundException;
import cart.repository.CartItemRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.OrderRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberCouponRepository memberCouponRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final CartItemRepository cartItemRepository,
            final MemberCouponRepository memberCouponRepository
    ) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    public Long save(final OrderSaveRequest request, final Long memberId) {
        final List<CartItem> items = cartItemRepository.findAllByIdsAndMemberId(request.getOrderItemIds(), memberId);
        final List<OrderItem> orderItems = toOrderItems(items);

        final MemberCoupon memberCoupon = memberCouponRepository.findById(request.getCouponId())
                .orElseGet(() -> MemberCoupon.empty(memberId));

        final Order order = Order.of(memberCoupon, memberId, orderItems);
        order.useCoupon();
        final Order saveOrder = orderRepository.save(order);

        cartItemRepository.deleteByIds(request.getOrderItemIds());
        return saveOrder.getId();
    }

    private List<OrderItem> toOrderItems(final List<CartItem> items) {
        return items.stream()
                .map(cartItem -> {
                    final Product product = cartItem.getProduct();
                    return new OrderItem(
                            product.getName(),
                            product.getImageUrl(),
                            product.getPrice(),
                            cartItem.getQuantity()
                    );
                })
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(final Long memberId) {
        return orderRepository.findAllByMemberId(memberId).stream()
                .map(OrderResponse::from)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(final Long id, final Long memberId) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        order.checkOwner(memberId);

        return OrderResponse.from(order);
    }
}

package cart.application;

import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.OrderException;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;
    private final CouponService couponService;

    public OrderService(final OrderRepository orderRepository,
                        final CartItemService cartItemService,
                        final CouponService couponService) {
        this.orderRepository = orderRepository;
        this.cartItemService = cartItemService;
        this.couponService = couponService;
    }

    public Long add(final long memberId, final OrderRequest orderRequest) {
        final List<Long> cartItemIds = orderRequest.getCartItemIds();
        final List<CartItem> cartItems = cartItemIds.stream()
                .map(id -> cartItemService.checkId(memberId, id))
                .collect(Collectors.toList());
        final Long couponId = orderRequest.getCouponId();

        final Order order = createOrder(memberId, cartItems, orderRequest.getDeliveryFee(), couponId)
                .complete(new Money(orderRequest.getTotalPrice()));

        cartItemService.remove(memberId, cartItemIds);
        return orderRepository.add(memberId, order);
    }

    private Order createOrder(final long memberId,
                              final List<CartItem> cartItems,
                              final long deliveryFee,
                              final Long couponId) {

        if (Objects.nonNull(couponId)) {
            final Coupon coupon = couponService.checkId(memberId, couponId);
            return new Order(coupon, new Money(deliveryFee), OrderItem.convert(cartItems));
        }
        return new Order(null, new Money(deliveryFee), OrderItem.convert(cartItems));
    }

    public List<OrderResponse> findOrdersByMember(final long memberId) {
        final List<Order> orders = orderRepository.findByMember(memberId);
        return OrderResponse.from(orders);
    }

    public OrderDetailResponse findOrderDetailById(final long memberId, final Long orderId) {
        final Order order = checkId(memberId, orderId);
        return OrderDetailResponse.from(order);
    }

    public void remove(final long memberId, final Long orderId) {
        checkId(memberId, orderId);
        orderRepository.remove(orderId);
    }

    public void cancel(final long memberId, final Long orderId) {
        final Order order = checkId(memberId, orderId);
        orderRepository.update(memberId, order.cancel());
    }

    private Order checkId(final long memberId, final Long orderId) {
        return orderRepository.findByIdForMember(memberId, orderId)
                .orElseThrow(() -> new OrderException.IllegalId(orderId));
    }
}

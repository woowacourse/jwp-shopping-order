package cart.order.domain.service;

import cart.cartitem.domain.CartItem;
import cart.cartitem.domain.CartItemRepository;
import cart.coupon.domain.Coupon;
import cart.coupon.domain.CouponValidator;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.order.domain.OrderRepository;
import cart.order.domain.OrderValidator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderPlaceService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderValidator orderValidator;
    private final CouponValidator couponValidator;

    public OrderPlaceService(
            OrderRepository orderRepository,
            CartItemRepository cartItemRepository,
            OrderValidator orderValidator,
            CouponValidator couponValidator
    ) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderValidator = orderValidator;
        this.couponValidator = couponValidator;
    }

    public Long placeOrder(Long memberId, List<CartItem> cartItems, List<Coupon> coupons) {
        couponValidator.validate(memberId, cartItems, coupons);
        orderValidator.validate(memberId, cartItems);
        Order order = new Order(memberId, toOrderItems(cartItems, coupons));
        Long id = orderRepository.save(order);
        for (CartItem cartItem : cartItems) {
            cartItemRepository.deleteById(cartItem.getId());
        }
        return id;
    }

    private static List<OrderItem> toOrderItems(List<CartItem> cartItems, List<Coupon> coupons) {
        return cartItems.stream()
                .map(it -> OrderItem.withCoupon(it, coupons))
                .collect(Collectors.toList());
    }
}

package cart.order.domain.service;

import cart.cartitem.domain.CartItem;
import cart.cartitem.domain.CartItemRepository;
import cart.coupon.domain.Coupon;
import cart.coupon.domain.CouponValidator;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.order.domain.OrderRepository;
import cart.order.domain.OrderValidator;
import cart.product.domain.Product;
import cart.product.domain.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderPlaceService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public OrderPlaceService(
            OrderRepository orderRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public Long placeOrder(Long memberId, List<CartItem> cartItems, List<Coupon> coupons) {
        CouponValidator couponValidator = new CouponValidator();
        OrderValidator orderValidator = new OrderValidator();

        couponValidator.validate(memberId, cartItems, coupons);

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId());
            orderValidator.validate(memberId, cartItem, product);
        }

        Order order = new Order(memberId, toOrderItems(cartItems, coupons));
        Long orderId = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            cartItemRepository.deleteById(cartItem.getId());
        }

        return orderId;
    }

    private List<OrderItem> toOrderItems(List<CartItem> cartItems, List<Coupon> coupons) {
        return cartItems.stream()
                .map(cartItem -> toOrderItem(cartItem, coupons))
                .collect(Collectors.toList());
    }

    private OrderItem toOrderItem(CartItem cartItem, List<Coupon> coupons) {
        return coupons.stream()
                .filter(coupon -> coupon.canApply(cartItem.getProductId()))
                .findFirst()
                .map(coupon -> OrderItem.withCoupon(cartItem, coupon))
                .orElseGet(() -> OrderItem.withoutCoupon(cartItem));
    }
}

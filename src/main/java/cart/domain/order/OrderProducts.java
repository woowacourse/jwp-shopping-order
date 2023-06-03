package cart.domain.order;

import cart.domain.carts.CartItem;

import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class OrderProducts {

    private static final int DEFAULT_PAYMENT = 0;

    private Long orderId;
    private final List<OrderProduct> orderProducts;

    public OrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public OrderProducts(long orderId, List<OrderProduct> orderProducts) {
        this.orderId = orderId;
        this.orderProducts = orderProducts;
    }

    public static OrderProducts of(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new OrderProduct.Builder()
                        .productId(cartItem.getProduct().getId())
                        .productName(cartItem.getProduct().getName())
                        .productPrice(cartItem.getProduct().getPrice())
                        .productImageUrl(cartItem.getProduct().getImageUrl())
                        .quantity(cartItem.getQuantity())
                        .totalPrice(cartItem.calculateTotalProductsPrice())
                        .build()
                ).collect(collectingAndThen(toList(), OrderProducts::new));
    }

    public int calculateTotalPrice() {
        return orderProducts.stream()
                .map(OrderProduct::getTotalPrice)
                .reduce(Integer::sum)
                .orElseGet(() -> DEFAULT_PAYMENT);
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public long getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "OrderProducts{" +
                "orderId=" + orderId +
                ", orderProducts=" + orderProducts +
                ", totalPrice=" + calculateTotalPrice() +
                '}';
    }
}

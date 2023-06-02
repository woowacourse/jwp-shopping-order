package cart.domain;

import java.util.List;

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

    public int calculateTotalPayment() {
        return orderProducts.stream()
                .map(OrderProduct::getPrice)
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
                '}';
    }
}

package cart.domain;

import java.util.List;

public class OrderItems {

    private static final int DEFAULT_PAYMENT = 0;

    private Long orderId;
    private final List<OrderItem> orderItems;

    public OrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public OrderItems(long orderId, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.orderItems = orderItems;
    }

    public int calculateTotalPayment() {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(Integer::sum)
                .orElseGet(() -> DEFAULT_PAYMENT);
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public long getOrderId() {
        return orderId;
    }
}

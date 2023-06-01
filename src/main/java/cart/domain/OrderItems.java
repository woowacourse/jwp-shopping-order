package cart.domain;

import java.util.List;

public class OrderItems {

    private final List<OrderItem> orderItems;

    public OrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public int calculateTotalPayment() {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(Integer::sum)
                .orElseGet(() -> 0);
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}

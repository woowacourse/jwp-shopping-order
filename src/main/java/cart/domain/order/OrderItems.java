package cart.domain.order;

import java.util.List;

public class OrderItems {
    private final List<OrderItem> orderItems;

    public OrderItems(final List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public int calculateOrderItemsPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getProductPrice)
                .sum();
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}

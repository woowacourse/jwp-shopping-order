package cart.domain.order;

import java.util.List;

public class OrderItems {

    private final List<OrderItem> orderItems;

    private OrderItems(final List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public static OrderItems from(final List<OrderItem> orderItems) {
        return new OrderItems(orderItems);
    }

    public List<OrderItem> getOrderItems() {
        return List.copyOf(orderItems);
    }
}

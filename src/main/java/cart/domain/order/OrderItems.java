package cart.domain.order;

import java.util.List;

public class OrderItems {

    private final List<OrderItem> orderItems;

    public OrderItems(final List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItem> getOrderItems() {
        return List.copyOf(orderItems);
    }
}

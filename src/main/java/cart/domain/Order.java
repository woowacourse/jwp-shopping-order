package cart.domain;

import java.util.List;

public class Order {

    private final Long id;
    private final List<OrderItem> orderItems;

    public Order(final Long id, final List<OrderItem> orderItems) {
        this.id = id;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}

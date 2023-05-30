package cart.domain;

import java.util.List;

public class Order {

    private Long id;
    private Money deliveryFee;
    private List<OrderItem> orderItems;

    public Order() {
    }

    public Order(final Long id, final List<OrderItem> orderItems) {
        this.id = id;
        this.orderItems = orderItems;
    }

    public Order(final Money deliveryFee, final List<OrderItem> orderItems) {
        this.deliveryFee = deliveryFee;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Money getDeliveryFee() {
        return deliveryFee;
    }
}

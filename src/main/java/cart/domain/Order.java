package cart.domain;

import java.util.List;

public class Order {

    private final Long id;
    private final List<OrderItem> orderItems;
    private Money deliveryFee;

    public Order(final Long id, final List<OrderItem> orderItems) {
        this.id = id;
        this.orderItems = orderItems;
    }

    public Order(final Long id, final List<OrderItem> orderItems, final Money deliveryFee) {
        this.id = id;
        this.orderItems = orderItems;
        this.deliveryFee = deliveryFee;
    }

    public Long totalPrice() {
        return orderItems.stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();
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

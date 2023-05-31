package cart.domain;

import cart.exception.OrderException;
import java.util.List;

public class Order {

    private final List<OrderItem> orderItems;
    private Long id;
    private Member member;
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

    public Order(final Member member, final List<OrderItem> orderItems, final Money deliveryFee) {
        this.member = member;
        this.orderItems = orderItems;
        this.deliveryFee = deliveryFee;
    }

    public void checkTotalPrice(final Long totalPrice) {
        final long totalPriceFromOrder = orderItems.stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();
        if (totalPriceFromOrder != totalPrice) {
            throw new OrderException.OutOfDatedProductPrice();
        }
    }

    public Long totalPrice() {
        return orderItems.stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Money getDeliveryFee() {
        return deliveryFee;
    }
}

package cart.domain;

import java.util.List;

public class Order {

    private Long id;
    private Member member;
    private Money deliveryFee;
    private List<OrderItem> orderItems;

    public Order() {
    }

    public Order(final Member member, final Money deliveryFee, final List<OrderItem> orderItems) {
        this.member = member;
        this.deliveryFee = deliveryFee;
        this.orderItems = orderItems;
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

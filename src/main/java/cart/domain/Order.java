package cart.domain;

import java.util.List;

public class Order {

    private final Long id;
    private final Member member;
    private final List<OrderItem> orderItems;

    public Order(final Long id, final Member member, final List<OrderItem> orderItems) {
        this.id = id;
        this.member = member;
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
}

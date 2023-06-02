package cart.domain;

import java.sql.Timestamp;

public class Order {

    private Long id;
    private final Member member;
    private final OrderItems orderItems;
    private final Payment payment;
    private Timestamp createdAt;

    public Order(Member member, OrderItems orderItems, Payment payment) {
        this.member = member;
        this.orderItems = orderItems;
        this.payment = payment;
    }

    public Order(long id, Member member, OrderItems orderItems, Payment payment, Timestamp createdAt) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.payment = payment;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }

    public Payment getPayment() {
        return payment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}

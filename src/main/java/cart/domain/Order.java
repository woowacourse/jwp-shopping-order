package cart.domain;

import java.time.LocalDateTime;

public class Order {
    private final Long id;
    private final Payment payment;
    private final OrderItems orderItems;
    private final Member member;
    private final LocalDateTime createdAt;

    public Order(final Long id, final LocalDateTime createdAt, final Payment payment, final OrderItems orderItems,
                 final Member member) {
        this.id = id;
        this.createdAt = createdAt;
        this.payment = payment;
        this.orderItems = orderItems;
        this.member = member;
    }

    public Order(final Payment payment, final OrderItems orderItems, final Member member) {
        this(null, null, payment, orderItems, member);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Payment getPayment() {
        return payment;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }

    public Member getMember() {
        return member;
    }
}

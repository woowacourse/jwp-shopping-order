package cart.domain.order;

import cart.domain.member.Member;
import cart.domain.payment.Payment;

import java.time.LocalDateTime;

public class Order {

    private Long id;
    private final Member member;
    private final OrderProducts orderProducts;
    private final Payment payment;
    private LocalDateTime createdAt;

    public Order(Member member, OrderProducts orderProducts, Payment payment) {
        this.member = member;
        this.orderProducts = orderProducts;
        this.payment = payment;
    }

    public Order(long id, Member member, OrderProducts orderProducts, Payment payment, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.orderProducts = orderProducts;
        this.payment = payment;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public OrderProducts getOrderProducts() {
        return orderProducts;
    }

    public Payment getPayment() {
        return payment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

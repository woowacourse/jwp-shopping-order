package cart.domain.order.domain;

import java.time.LocalDateTime;

import cart.domain.member.domain.Member;

public class Order {

    private Long id;
    private final Member member;
    private final int totalPrice;
    private final LocalDateTime createdAt;

    public Order(Member member, int totalPrice, LocalDateTime createdAt) {
        this.member = member;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public Order(final Long id, final Member member,
                 final int totalPrice, final LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member +
                ", totalPrice=" + totalPrice +
                ", createdAt=" + createdAt +
                '}';
    }
}

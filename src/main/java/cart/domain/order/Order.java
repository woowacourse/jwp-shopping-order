package cart.domain.order;

import cart.domain.Member;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {
    private final Long id;
    private final Member member;
    private final int paymentPrice;
    private final int totalPrice;
    private final int point;
    private final LocalDateTime createdAt;

    public Order(Long id, int paymentPrice, int totalPrice, int point, Member member, LocalDateTime createdAt) {
        this.id = id;
        this.paymentPrice = paymentPrice;
        this.totalPrice = totalPrice;
        this.point = point;
        this.member = member;
        this.createdAt = createdAt;
    }

    public Order(int paymentPrice, int totalPrice, int point, Member member) {
        this(null, paymentPrice, totalPrice, point, member, null);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public int getPaymentPrice() {
        return paymentPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPoint() {
        return point;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

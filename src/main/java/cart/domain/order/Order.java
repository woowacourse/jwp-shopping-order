package cart.domain.order;

import cart.domain.Member;

import java.util.Objects;

public class Order {
    private final Long id;
    private final Member member;
    private final int paymentPrice;
    private final int totalPrice;
    private final int point;

    public Order(final Long id, final int paymentPrice, final int totalPrice, final int point, final Member member) {
        this.id = id;
        this.paymentPrice = paymentPrice;
        this.totalPrice = totalPrice;
        this.point = point;
        this.member = member;
    }

    public Order(final int paymentPrice, final int totalPrice, final int point, final Member member) {
        this(null, paymentPrice, totalPrice, point, member);
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

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member +
                ", paymentPrice=" + paymentPrice +
                ", totalPrice=" + totalPrice +
                ", point=" + point +
                '}';
    }
}

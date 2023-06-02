package cart.domain.order;

import cart.domain.Member;

public class Order {
    private final Long id;
    private final Member member;
    private final int paymentPrice;
    private final int totalPrice;
    private final int point;

    public Order(final Long id, final Member member, final int paymentPrice, final int totalPrice, final int point) {
        this.id = id;
        this.member = member;
        this.paymentPrice = paymentPrice;
        this.totalPrice = totalPrice;
        this.point = point;
    }

    public Order(final int paymentPrice, final int totalPrice, final int point, final Member member) {
        this(null, member, paymentPrice, totalPrice, point);
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
}

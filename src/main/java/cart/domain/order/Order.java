package cart.domain.order;

import cart.domain.Member;

import java.util.List;

public class Order {
    private final Long id;
    private final Member member;
    private final List<OrderItem> orderItems;
    private final int paymentPrice;
    private final int totalPrice;
    private final int point;

    public Order(final Long id, final Member member, final List<OrderItem> orderItems, final int paymentPrice, final int totalPrice, final int point) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.paymentPrice = paymentPrice;
        this.totalPrice = totalPrice;
        this.point = point;
    }

    public Order(final int paymentPrice, final int totalPrice, final int point, final Member member, final List<OrderItem> orderItems) {
        this(null, member, orderItems, paymentPrice, totalPrice, point);
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

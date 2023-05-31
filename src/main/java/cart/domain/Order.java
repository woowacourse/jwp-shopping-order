package cart.domain;

import java.util.List;

public class Order {
    private Long orderId;
    private final List<OrderDetail> orderDetails;
    private final Payment payment;
    private final Point point;
    private final Member member;

    public Order(final List<OrderDetail> orderDetails, final Payment payment, final Point point, final Member member) {
        this.orderDetails = orderDetails;
        this.payment = payment;
        this.point = point;
        this.member = member;
    }

    public Order(final Long orderId, final List<OrderDetail> orderDetails, final Payment payment, final Point point, final Member member) {
        this.orderId = orderId;
        this.orderDetails = orderDetails;
        this.payment = payment;
        this.point = point;
        this.member = member;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Payment getPayment() {
        return payment;
    }

    public Point getPoint() {
        return point;
    }

    public Member getMember() {
        return member;
    }
}

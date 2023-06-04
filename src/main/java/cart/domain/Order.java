package cart.domain;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private Long orderId;
    private final List<OrderDetail> orderDetails;
    private final Payment payment;
    private final Point point;
    private final Member member;

    public Order(final List<OrderDetail> orderDetails, final Payment payment, final Point point, final Member member) {
        validatePayment(orderDetails, payment, point);
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

    private void validatePayment(List<OrderDetail> orderDetails, Payment payment, Point point) {
        final int totalPrice = orderDetails.stream()
                .mapToInt(orderDetail -> (int) (orderDetail.getProduct().getPrice() * orderDetail.getQuantity()))
                .sum();
        final BigDecimal requestPayment = payment.getPayment().add(point.getPoint());
        System.out.println("totalPrice = " + totalPrice);
        System.out.println("requestPayment = " + requestPayment);
        if (!BigDecimal.valueOf(totalPrice).equals(requestPayment)) {
            throw new IllegalArgumentException("결제금액과 총 가격이 일치하지 않습니다");
        }
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

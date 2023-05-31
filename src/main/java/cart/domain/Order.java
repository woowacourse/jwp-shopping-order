package cart.domain;

import cart.domain.common.Money;
import cart.domain.member.Member;

import java.util.List;
import java.util.Objects;

public class Order {

    private final Long id;
    private final Member member;
    private final Money payment;
    private final Point point;
    private final List<OrderItem> orderItems;

    private Order(final Long id, final Member member, final Money payment, final Point point, final List<OrderItem> orderItems) {
        validatePointAvailability(member, point);
        validatePayment(payment, point, orderItems);
        this.id = id;
        this.member = member;
        this.payment = payment;
        this.point = point;
        this.orderItems = orderItems;
    }

    public static Order from(final Long id, final Member member, final int payment, final int point, final List<OrderItem> orderItems) {
        return new Order(id, member, Money.valueOf(payment), Point.valueOf(point), orderItems);
    }

    public static Order from(final Member member, final int payment, final int point, final List<OrderItem> orderItems) {
        return new Order(null, member, Money.valueOf(payment), Point.valueOf(point), orderItems);
    }

    private void validatePointAvailability(final Member member, final Point point) {
        if (!member.canUsePoint(point)) {
            throw new IllegalArgumentException("가용 포인트를 초과했습니다.");
        }
    }

    private void validatePayment(final Money payment, final Point point, final List<OrderItem> orderItems) {
        final Money totalPrice = Money.valueOf(orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum());
        final Money totalPayment = payment.add(point.getMoney());
        if (!totalPrice.equals(totalPayment)) {
            throw new IllegalArgumentException("총 결제 금액이 총 상품 가격과 맞지 않습니다.");
        }
    }

    public Member calculateMemberPoint(final Point savePoint) {
        return member.calculatePoint(point, savePoint);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public int getPaymentAmount() {
        return payment.getAmount();
    }

    public Money getPayment() {
        return payment;
    }

    public int getPointAmount() {
        return point.getMoneyAmount();
    }

    public Point getPoint() {
        return point;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Order order = (Order) o;
        return Objects.equals(member, order.member) && Objects.equals(payment, order.payment) && Objects.equals(point, order.point) && Objects.equals(orderItems, order.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, payment, point, orderItems);
    }

    @Override
    public String toString() {
        return "Order{" +
                "member=" + member +
                ", payment=" + payment +
                ", point=" + point +
                ", orderItems=" + orderItems +
                '}';
    }
}

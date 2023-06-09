package cart.persistence.order.dto;

import cart.domain.member.Member;
import cart.domain.point.Point;

import java.sql.Timestamp;

public class OrderDto {
    private final Long id;
    private final Member member;
    private final int paymentPrice;
    private final Point point;
    private final Timestamp createdAt;



    public OrderDto(final Long id, final Member member, final int paymentPrice, final Point point, final Timestamp createdAt) {
        this.id = id;
        this.member = member;
        this.paymentPrice = paymentPrice;
        this.point = point;
        this.createdAt = createdAt;
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

    public Point getPoint() {
        return point;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}

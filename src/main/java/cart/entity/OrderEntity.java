package cart.entity;

import cart.domain.order.Order;

import java.math.BigDecimal;

public class OrderEntity {

    private final Long id;
    private final BigDecimal deliveryFee;
    private final Long memberCouponId;
    private final Long memberId;

    public OrderEntity(final Long id, final BigDecimal deliveryFee, final Long memberCouponId, final Long memberId) {
        this.id = id;
        this.deliveryFee = deliveryFee;
        this.memberCouponId = memberCouponId;
        this.memberId = memberId;
    }

    public OrderEntity(final BigDecimal deliveryFee, final Long memberCouponId, final Long memberId) {
        this(null, deliveryFee, memberCouponId, memberId);
    }

    public static OrderEntity from(final Order order) {
        return new OrderEntity(order.getId(), order.getDeliveryFee().getValue(), order.getMemberCoupon().getId(), order.getMemberId());
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public Long getMemberCouponId() {
        return memberCouponId;
    }

    public Long getMemberId() {
        return memberId;
    }
}

package cart.entity;

import cart.domain.order.Order;

public class OrderEntity {

    private final Long id;
    private final long deliveryFee;
    private final Long couponId;
    private final Long memberId;

    public OrderEntity(final Long id, final long deliveryFee, final Long couponId, final Long memberId) {
        this.id = id;
        this.deliveryFee = deliveryFee;
        this.couponId = couponId;
        this.memberId = memberId;
    }

    public OrderEntity(final long deliveryFee, final Long couponId, final Long memberId) {
        this(null, deliveryFee, couponId, memberId);
    }

    public static OrderEntity from(final Order order) {
        return new OrderEntity(order.getId(), order.getDeliveryFee().getValue(), order.getMemberCoupon().getId(), order.getMemberId());
    }

    public Long getId() {
        return id;
    }

    public long getDeliveryFee() {
        return deliveryFee;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getMemberId() {
        return memberId;
    }
}

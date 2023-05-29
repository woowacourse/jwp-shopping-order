package cart.entity;

import cart.domain.Order;

public class OrderEntity {
//        `id`           long PRIMARY KEY NOT NULL AUTO_INCREMENT,
//    `delivery_fee` long             NOT NULL,
//            `coupon_id`    long,
//            `member_id`    long             NOT NULL,
//
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

    public static OrderEntity from(final Order order) {
        return new OrderEntity(order.getId(), order.getDeliveryFee(), order.getMemberCoupon().getId(), order.getMember().getId());
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

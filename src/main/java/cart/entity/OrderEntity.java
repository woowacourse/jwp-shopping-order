package cart.entity;

import cart.domain.coupon.Coupon;
import cart.domain.order.Order;

public class OrderEntity {

    private final Long id;
    private final Long deliveryFee;
    private final Long couponId;
    private final Long memberId;

    public OrderEntity(final Long deliveryFee, final Long couponId, final Long memberId) {
        this(null, deliveryFee, couponId, memberId);
    }

    public OrderEntity(final Long id, final Long deliveryFee, Long couponId, final Long memberId) {
        this.id = id;
        this.deliveryFee = deliveryFee;
        this.couponId = couponId;
        this.memberId = memberId;
    }

    public static OrderEntity from(final Order order) {
        if (order.getCoupon() == Coupon.EMPTY) {
            return new OrderEntity(order.getDeliveryFee().getLongValue(), null, order.getMemberId());
        }
        return new OrderEntity(order.getDeliveryFee().getLongValue(), order.getCoupon().getId(), order.getMemberId());
    }

    public Long getId() {
        return id;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getMemberId() {
        return memberId;
    }
}

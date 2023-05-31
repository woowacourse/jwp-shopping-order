package cart.domain.coupon.dto;

import cart.domain.coupon.Coupon;

public class CouponWithId {

    private final Long couponId;
    private final Coupon coupon;

    public CouponWithId(final Long couponId, final Coupon coupon) {
        this.couponId = couponId;
        this.coupon = coupon;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Coupon getCoupon() {
        return coupon;
    }
}

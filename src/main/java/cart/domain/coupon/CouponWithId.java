package cart.domain.coupon;

import java.time.LocalDateTime;

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

    public boolean isNotExpired(final LocalDateTime targetTime) {
        return coupon.isNotExpired(targetTime);
    }
}

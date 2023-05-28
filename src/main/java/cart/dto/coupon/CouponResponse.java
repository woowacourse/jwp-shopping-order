package cart.dto.coupon;

import cart.domain.coupon.Coupon;

public class CouponResponse {

    private final long couponId;
    private final String couponName;

    private CouponResponse(final long couponId, final String couponName) {
        this.couponId = couponId;
        this.couponName = couponName;
    }

    public static CouponResponse from(final Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getName());
    }

    public long getCouponId() {
        return couponId;
    }

    public String getCouponName() {
        return couponName;
    }
}

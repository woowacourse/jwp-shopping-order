package cart.cart.presentation.dto;

import cart.coupon.Coupon;

public class CouponResponse {
    private long couponId;
    private String couponName;

    public CouponResponse() {
    }

    private CouponResponse(long couponId, String couponName) {
        this.couponId = couponId;
        this.couponName = couponName;
    }

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getName());
    }

    public long getCouponId() {
        return couponId;
    }

    public String getCouponName() {
        return couponName;
    }
}

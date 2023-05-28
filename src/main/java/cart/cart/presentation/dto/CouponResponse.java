package cart.cart.presentation.dto;

public class CouponResponse {
    private long couponId;
    private String couponName;

    public CouponResponse() {
    }

    private CouponResponse(long couponId, String couponName) {
        this.couponId = couponId;
        this.couponName = couponName;
    }

    public static CouponResponse from(long couponId, String couponName) {
        return new CouponResponse(couponId, couponName);
    }

    public long getCouponId() {
        return couponId;
    }

    public String getCouponName() {
        return couponName;
    }
}

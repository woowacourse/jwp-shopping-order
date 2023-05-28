package cart.dto.coupon;

public class CouponResponse {

    private final long couponId;
    private final String couponName;

    public CouponResponse(final long couponId, final String couponName) {
        this.couponId = couponId;
        this.couponName = couponName;
    }

    public long getCouponId() {
        return couponId;
    }

    public String getCouponName() {
        return couponName;
    }
}

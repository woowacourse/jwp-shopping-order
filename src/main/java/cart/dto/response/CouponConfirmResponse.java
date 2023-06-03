package cart.dto.response;

public class CouponConfirmResponse {
    private final CouponResponse coupon;

    private CouponConfirmResponse(CouponResponse coupon) {
        this.coupon = coupon;
    }

    public static CouponConfirmResponse from(CouponResponse couponResponse) {
        return new CouponConfirmResponse(couponResponse);
    }

    public CouponResponse getCoupon() {
        return coupon;
    }
}

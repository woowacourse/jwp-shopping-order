package cart.dto.response;

public class CouponConfirmResponse {
    private final CouponResponse coupon;

    public CouponConfirmResponse(CouponResponse coupon) {
        this.coupon = coupon;
    }

    public CouponResponse getCoupon() {
        return coupon;
    }
}

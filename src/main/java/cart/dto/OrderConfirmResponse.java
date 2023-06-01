package cart.dto;

public class OrderConfirmResponse {
    private final CouponResponse coupon;

    public OrderConfirmResponse(CouponResponse coupon) {
        this.coupon = coupon;
    }

    public CouponResponse getCoupon() {
        return coupon;
    }
}

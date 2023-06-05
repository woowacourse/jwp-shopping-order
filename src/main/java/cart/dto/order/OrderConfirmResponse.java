package cart.dto.order;

import cart.dto.CouponResponse;

public class OrderConfirmResponse {
    private final CouponResponse coupon;

    public OrderConfirmResponse(CouponResponse coupon) {
        this.coupon = coupon;
    }

    public CouponResponse getCoupon() {
        return coupon;
    }
}

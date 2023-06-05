package cart.dto.order;

import cart.dto.CouponResponse;
import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderConfirmResponse {
    private final CouponResponse coupon;

    @JsonCreator
    public OrderConfirmResponse(CouponResponse coupon) {
        this.coupon = coupon;
    }

    public CouponResponse getCoupon() {
        return coupon;
    }
}

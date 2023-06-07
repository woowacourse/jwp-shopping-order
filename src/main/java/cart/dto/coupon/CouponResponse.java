package cart.dto.coupon;

import cart.domain.coupon.Coupon;
import cart.dto.history.OrderedCouponHistory;

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

    public static CouponResponse from(final OrderedCouponHistory orderedCouponHistory) {
        return new CouponResponse(orderedCouponHistory.getId(), orderedCouponHistory.getName());
    }

    public long getCouponId() {
        return couponId;
    }

    public String getCouponName() {
        return couponName;
    }
}

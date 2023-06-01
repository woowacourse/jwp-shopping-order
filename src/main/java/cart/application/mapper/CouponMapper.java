package cart.application.mapper;

import cart.application.dto.coupon.CouponResponse;
import cart.domain.coupon.dto.CouponWithId;

public class CouponMapper {

    public static CouponResponse convertCouponResponse(final CouponWithId couponWithId) {
        return new CouponResponse(couponWithId.getCouponId(), couponWithId.getCoupon().name(),
            couponWithId.getCoupon().discountRate(), couponWithId.getCoupon().expiredAt());
    }
}

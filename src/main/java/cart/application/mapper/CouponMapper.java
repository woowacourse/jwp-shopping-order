package cart.application.mapper;

import cart.application.dto.coupon.CouponResponse;
import cart.domain.coupon.Coupon;

public class CouponMapper {

    public static CouponResponse convertCouponResponse(final Coupon Coupon) {
        return new CouponResponse(Coupon.couponId(), Coupon.name(),
            Coupon.discountRate(), Coupon.expiredAt());
    }

    public static CouponResponse convertCouponResponse(final Long id, final Coupon coupon) {
        return new CouponResponse(id, coupon.name(), coupon.discountRate(), coupon.expiredAt());
    }
}

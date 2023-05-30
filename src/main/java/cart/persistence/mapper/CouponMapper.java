package cart.persistence.mapper;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.dto.CouponWithId;
import cart.persistence.entity.CouponEntity;

public class CouponMapper {

    public static CouponWithId convertCouponWithId(final CouponEntity couponEntity) {
        return new CouponWithId(couponEntity.getId(), couponEntity.getName(),
            couponEntity.getDiscountRate(), couponEntity.getPeriod(), couponEntity.getExpiredDate());
    }

    public static Coupon convertCoupon(final CouponEntity couponEntity) {
        return Coupon.create(couponEntity.getName(),
            couponEntity.getDiscountRate(), couponEntity.getPeriod(), couponEntity.getExpiredDate());
    }

    public static CouponEntity convertCouponEntity(final Coupon coupon) {
        return new CouponEntity(coupon.name(), coupon.discountRate(), coupon.period(), coupon.expiredDate());
    }
}

package cart.coupon.infrastructure.mapper;

import cart.coupon.domain.Coupon;
import cart.coupon.domain.CouponType;
import cart.coupon.domain.DiscountPolicy;
import cart.coupon.domain.DiscountType;
import cart.coupon.domain.FixDiscountPolicy;
import cart.coupon.domain.GeneralCouponType;
import cart.coupon.domain.RateDiscountPolicy;
import cart.coupon.domain.SpecificCouponType;
import cart.coupon.domain.TargetType;
import cart.coupon.infrastructure.entity.CouponEntity;

public class CouponEntityMapper {

    public static Coupon toDomain(CouponEntity coupon) {
        return new Coupon(
                coupon.getId(),
                coupon.getName(),
                getDiscountPolicy(coupon),
                getCouponType(coupon),
                coupon.getMemberId());
    }

    private static DiscountPolicy getDiscountPolicy(CouponEntity coupon) {
        DiscountType discountType = coupon.getDiscountType();
        if (discountType == DiscountType.RATE) {
            return new RateDiscountPolicy(coupon.getCouponValue());
        }
        return new FixDiscountPolicy(coupon.getCouponValue());
    }

    private static CouponType getCouponType(CouponEntity coupon) {
        TargetType targetType = coupon.getTargetType();
        if (targetType == TargetType.ALL) {
            return new GeneralCouponType();
        }
        return new SpecificCouponType(coupon.getTargetProductId());
    }

    public static CouponEntity toEntity(Coupon coupon) {
        return new CouponEntity(
                coupon.getId(),
                coupon.getName(),
                coupon.getMemberId(),
                coupon.discountType(),
                coupon.targetType(),
                getProductId(coupon.getCouponType()),
                coupon.value());
    }

    private static Long getProductId(CouponType couponType) {
        if (couponType.getTargetType() == TargetType.ALL) {
            return null;
        }
        return ((SpecificCouponType) couponType).getProductId();
    }
}

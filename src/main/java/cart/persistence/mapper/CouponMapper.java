package cart.persistence.mapper;

import cart.domain.coupon.Coupon;
import cart.persistence.dao.dto.MemberCouponDto;
import cart.persistence.dao.dto.OrderDto;
import cart.persistence.entity.CouponEntity;

public class CouponMapper {

    public static Coupon convertCoupon(final CouponEntity couponEntity) {
        return Coupon.create(couponEntity.getId(), couponEntity.getName(),
            couponEntity.getDiscountRate(), couponEntity.getPeriod(), couponEntity.getExpiredAt());
    }

    public static Coupon convertCoupon(final OrderDto order) {
        return Coupon.create(order.getCouponId(), order.getCouponName(), order.getCouponDiscountRate(),
            order.getCouponPeriod(), order.getCouponExpiredAt());
    }

    public static Coupon convertCoupon(final MemberCouponDto memberCouponDto) {
        return Coupon.create(memberCouponDto.getCouponId(), memberCouponDto.getCouponName(),
            memberCouponDto.getDiscountRate(), memberCouponDto.getCouponPeriod(), memberCouponDto.getExpiredAt());
    }

    public static CouponEntity convertCouponEntity(final Coupon coupon) {
        return new CouponEntity(coupon.name(), coupon.discountRate(), coupon.period(), coupon.expiredAt());
    }
}

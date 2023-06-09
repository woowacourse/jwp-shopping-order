package cart.db.mapper;

import cart.db.entity.CouponEntity;
import cart.domain.coupon.Coupon;

public class CouponMapper {

    public static Coupon toDomain(final CouponEntity couponEntity) {
        return new Coupon(
                couponEntity.getId(),
                couponEntity.getName(),
                couponEntity.getDiscountRate(),
                couponEntity.getPeriod(),
                couponEntity.getExpiredAt()
        );
    }
}

package cart.persistence.repository;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.DeductionCoupon;
import cart.domain.coupon.PercentCoupon;
import cart.persistence.entity.CouponEntity;

import java.util.Arrays;
import java.util.function.Function;

public enum CouponMapper {
    PERCENTAGE_COUPON("percentage", couponEntity -> new PercentCoupon(
            couponEntity.getId(),
            couponEntity.getName(),
            couponEntity.getDiscountPercent(),
            couponEntity.getMinimumPrice())
    ),
    DEDUCTION_COUPON("deduction", couponEntity -> new DeductionCoupon(
            couponEntity.getId(),
            couponEntity.getName(),
            couponEntity.getDiscountAmount(),
            couponEntity.getMinimumPrice())
    );

    private final String typeName;
    private final Function<CouponEntity, Coupon> mapper;

    CouponMapper(String typeName, Function<CouponEntity, Coupon> mapper) {
        this.typeName = typeName;
        this.mapper = mapper;
    }

    public static Coupon mapToCoupon(CouponEntity couponEntity) {
        CouponMapper couponMapper = CouponMapper.findByTypeName(couponEntity.getDiscountType());
        return couponMapper.map(couponEntity);
    }

    public static CouponMapper findByTypeName(String typeName) {
        return Arrays.stream(CouponMapper.values())
                .filter(couponMapper -> couponMapper.isSameName(typeName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("요청한 할인 타입이 존재하지 않습니다."));
    }

    public boolean isSameName(String typeName) {
        return this.typeName.equals(typeName);
    }

    public Coupon map(CouponEntity couponEntity) {
        return mapper.apply(couponEntity);
    }
}

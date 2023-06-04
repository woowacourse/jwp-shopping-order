package cart.domain;

import cart.exception.NoExpectedException;

import java.util.Arrays;
import java.util.Objects;

public enum CouponType {
    FIXED_AMOUNT,
    FIXED_PERCENTAGE;

    public static CouponType from(final String value) {
        return Arrays.stream(values())
                .filter(couponType -> Objects.equals(couponType.name(), value))
                .findAny()
                .orElseThrow(() -> new NoExpectedException("해당 타입 쿠폰이 없습니다,"));
    }

    public static CouponType from(final Coupon coupon) {
        if (coupon instanceof PercentageCoupon) {
            return FIXED_PERCENTAGE;
        }
        return FIXED_AMOUNT;
    }
}

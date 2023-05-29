package cart.domain.coupon;

import static cart.exception.ErrorCode.COUPON_DISCOUNT_RATE_RANGE;

import cart.exception.BadRequestException;
import java.util.Objects;

public class CouponDiscountRate {

    private static final int DISCOUNT_RATE_MIN_VALUE = 5, DISCOUNT_RATE_MAX_VALUE = 90;

    private final int discountRate;

    private CouponDiscountRate(final int discountRate) {
        this.discountRate = discountRate;
    }

    public static CouponDiscountRate create(final int discountRate) {
        validateRange(discountRate);
        return new CouponDiscountRate(discountRate);
    }

    private static void validateRange(final int discountRate) {
        if (discountRate < DISCOUNT_RATE_MIN_VALUE || discountRate > DISCOUNT_RATE_MAX_VALUE) {
            throw new BadRequestException(COUPON_DISCOUNT_RATE_RANGE);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CouponDiscountRate that = (CouponDiscountRate) o;
        return discountRate == that.discountRate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountRate);
    }

    public int getDiscountRate() {
        return discountRate;
    }
}

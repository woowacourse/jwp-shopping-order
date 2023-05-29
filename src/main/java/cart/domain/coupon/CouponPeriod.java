package cart.domain.coupon;

import static cart.exception.ErrorCode.COUPON_PERIOD_RANGE;

import cart.exception.BadRequestException;
import java.util.Objects;

public class CouponPeriod {
    private static final int PERIOD_MIN_RANGE = 1, PERIOD_MAX_RANGE = 365;

    private final int period;

    private CouponPeriod(final int period) {
        this.period = period;
    }

    public static CouponPeriod create(final int period) {
        validateRange(period);
        return new CouponPeriod(period);
    }

    private static void validateRange(final int period) {
        if (period < PERIOD_MIN_RANGE || period > PERIOD_MAX_RANGE) {
            throw new BadRequestException(COUPON_PERIOD_RANGE);
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
        final CouponPeriod that = (CouponPeriod) o;
        return period == that.period;
    }

    @Override
    public int hashCode() {
        return Objects.hash(period);
    }

    public int getPeriod() {
        return period;
    }
}

package cart.domain.coupon;

import cart.exception.GlobalException;

public class Period {
    private static final int MIN_PERIOD = 1;
    private static final int MAX_PERIOD = 365;

    private final int period;

    public Period(int period) {
        validate(period);
        this.period = period;
    }

    private void validate(int period) {
        if (period < MIN_PERIOD || period > MAX_PERIOD) {
            throw new GlobalException("사용 가능 기간은 1일 ~ 365일 사이여야 합니다.");
        }
    }

    public int getPeriod() {
        return period;
    }
}

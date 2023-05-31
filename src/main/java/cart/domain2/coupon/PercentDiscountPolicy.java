package cart.domain2.coupon;

import cart.domain2.common.Money;
import cart.exception.InvalidDiscountPolicyException;

public class PercentDiscountPolicy implements DiscountPolicy {

    private static final long MINIMUM_RANGE = 0;
    private static final long MAXIMUM_RANGE = 100;
    private static final long RATIO_DIVISOR = 100;

    private void validate(final long discountValue) {
        if (discountValue < MINIMUM_RANGE || MAXIMUM_RANGE < discountValue) {
            throw new InvalidDiscountPolicyException("비율 할인 정책의 비율은 0 ~ 100 사이여야합니다.");
        }
    }

    @Override
    public Money calculatePrice(final Long discountValue, final Money price) {
        validate(discountValue);
        final Money subtrahend = price.times(Double.valueOf(discountValue) / RATIO_DIVISOR);
        final Money result = price.minus(subtrahend);
        if (result.isGreaterThanOrEqual(Money.ZERO)) {
            return result;
        }
        return Money.ZERO;
    }

    @Override
    public Money calculateDeliveryFee(final Long discountValue, final Money deliveryFee) {
        return deliveryFee;
    }
}

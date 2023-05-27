package cart.domain.coupon;

import cart.domain.common.Money;
import cart.exception.InvalidDiscountPolicyException;

public class PercentDiscountPolicy implements DiscountPolicy {

    private static final int MINIMUM_RANGE = 0;
    private static final int MAXIMUM_RANGE = 100;
    private static final int RATIO_DIVISOR = 100;

    private final DiscountPolicyType discountPolicyType;
    private final int discountPercent;

    public PercentDiscountPolicy(final int discountPercent) {
        validate(discountPercent);
        this.discountPolicyType = DiscountPolicyType.PERCENT;
        this.discountPercent = discountPercent;
    }

    private void validate(final int discountPercent) {
        if (discountPercent < MINIMUM_RANGE || MAXIMUM_RANGE < discountPercent) {
            throw new InvalidDiscountPolicyException("비율 할인 정책의 비율은 0 ~ 100 사이여야합니다.");
        }
    }

    @Override
    public Money calculatePrice(final Money price) {
        final Money subtrahend = price.times(Double.valueOf(discountPercent) / RATIO_DIVISOR);
        return price.minus(subtrahend);
    }

    @Override
    public Money calculateDeliveryFee(final Money deliveryFee) {
        return deliveryFee;
    }

    @Override
    public DiscountPolicyType getDiscountPolicyType() {
        return discountPolicyType;
    }

    @Override
    public Money getDiscountPrice() {
        return Money.ZERO;
    }

    @Override
    public int getDiscountPercent() {
        return discountPercent;
    }

    @Override
    public boolean isDiscountDeliveryFee() {
        return false;
    }
}

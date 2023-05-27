package cart.domain.coupon;

import cart.exception.InvalidDiscountPolicyException;

public class PercentDiscountPolicy implements DiscountPolicy {

    private static final long MINIMUM_RANGE = 0;
    private static final long MAXIMUM_RANGE = 100;

    private final DiscountPolicyType discountPolicyType;
    private final long discountPercent;

    public PercentDiscountPolicy(final long discountPercent) {
        validate(discountPercent);
        this.discountPolicyType = DiscountPolicyType.PERCENT;
        this.discountPercent = discountPercent;
    }

    private void validate(final long discountPercent) {
        if (discountPercent < MINIMUM_RANGE || MAXIMUM_RANGE < discountPercent) {
            throw new InvalidDiscountPolicyException("비율 할인 정책의 비율은 0 ~ 100 사이여야합니다.");
        }
    }

    @Override
    public DiscountPolicyType getDiscountPolicyType() {
        return discountPolicyType;
    }

    @Override
    public long getDiscountPrice() {
        return 0;
    }

    @Override
    public long getDiscountPercent() {
        return discountPercent;
    }

    @Override
    public boolean isDiscountDeliveryFee() {
        return false;
    }
}

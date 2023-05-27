package cart.domain.coupon;

import cart.domain.common.Money;

public class NoneDiscountCondition implements DiscountCondition {

    private final DiscountConditionType discountConditionType;

    public NoneDiscountCondition() {
        this.discountConditionType = DiscountConditionType.NONE;
    }

    @Override
    public boolean isSatisfiedBy(final Money totalPrice) {
        return true;
    }

    @Override
    public DiscountConditionType getDiscountConditionType() {
        return discountConditionType;
    }
}

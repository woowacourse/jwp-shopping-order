package cart.domain.coupon;

import cart.domain.common.Money;

public class MinimumPriceDiscountCondition implements DiscountCondition {

    private final DiscountConditionType discountConditionType;
    private final Money minimumPrice;

    public MinimumPriceDiscountCondition(final long minimumPrice) {
        this.discountConditionType = DiscountConditionType.MINIMUM_PRICE;
        this.minimumPrice = Money.from(minimumPrice);
    }

    @Override
    public boolean isSatisfiedBy(final Money totalPrice) {
        return totalPrice.isGreaterThanOrEqual(minimumPrice);
    }

    @Override
    public DiscountConditionType getDiscountConditionType() {
        return discountConditionType;
    }

    @Override
    public Money getMinimumPrice() {
        return minimumPrice;
    }
}

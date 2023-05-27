package cart.domain.coupon;

public class MinimumPriceDiscountCondition implements DiscountCondition {

    private final DiscountConditionType discountConditionType;
    private final long minimumPrice;

    public MinimumPriceDiscountCondition(final long minimumPrice) {
        this.discountConditionType = DiscountConditionType.MINIMUM_PRICE;
        this.minimumPrice = minimumPrice;
    }

    @Override
    public DiscountConditionType getDiscountConditionType() {
        return discountConditionType;
    }
}

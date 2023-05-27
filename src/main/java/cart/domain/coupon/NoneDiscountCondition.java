package cart.domain.coupon;

public class NoneDiscountCondition implements DiscountCondition {

    private final DiscountConditionType discountConditionType;

    public NoneDiscountCondition() {
        this.discountConditionType = DiscountConditionType.NONE;
    }

    @Override
    public DiscountConditionType getDiscountConditionType() {
        return discountConditionType;
    }
}

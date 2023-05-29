package coupon.domain;

public class AlwaysDiscountCondition implements DiscountCondition {

    @Override
    public boolean isSatisfiedBy(Order order) {
        return true;
    }
}

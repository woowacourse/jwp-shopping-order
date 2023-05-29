package coupon.domain.discountcondition;

import coupon.domain.Order;

public class AlwaysDiscountCondition implements DiscountCondition {

    @Override
    public boolean isSatisfiedBy(Order order) {
        return true;
    }
}

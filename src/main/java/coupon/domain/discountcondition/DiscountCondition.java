package coupon.domain.discountcondition;

import coupon.domain.Order;

public interface DiscountCondition {

    boolean isSatisfiedBy(Order order);
}

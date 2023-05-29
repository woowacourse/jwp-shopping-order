package coupon.domain.discountcondition;

import coupon.domain.Order;

public interface DiscountCondition {

    DiscountCondition NoneDiscountCondition = order -> false;
    DiscountCondition AlwaysDiscountCondition = order -> true;

    boolean isSatisfiedBy(Order order);
}

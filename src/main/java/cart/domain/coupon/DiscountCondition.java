package cart.domain.coupon;

import cart.domain.common.Money;

public interface DiscountCondition {

    boolean isSatisfiedBy(final Money totalPrice);

    DiscountConditionType getDiscountConditionType();
}

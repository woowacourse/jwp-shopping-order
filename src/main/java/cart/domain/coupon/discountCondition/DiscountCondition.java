package cart.domain.coupon.discountCondition;

import cart.domain.TotalPrice;

public interface DiscountCondition {

    boolean isCondition(final TotalPrice orderPrice);

    long getMinimumPrice();
}

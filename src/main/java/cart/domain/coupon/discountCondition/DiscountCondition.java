package cart.domain.coupon.discountCondition;

import cart.domain.OrderPrice;

public interface DiscountCondition {

    boolean isCondition(final OrderPrice orderPrice);

    long getMinimumPrice();
}

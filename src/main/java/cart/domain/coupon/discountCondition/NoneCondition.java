package cart.domain.coupon.discountCondition;

import cart.domain.OrderPrice;

public class NoneCondition implements DiscountCondition {
    @Override
    public boolean isCondition(final OrderPrice orderPrice) {
        return true;
    }

    @Override
    public long getMinimumPrice() {
        return 0;
    }
}

package cart.domain.coupon.discountCondition;

import cart.domain.TotalPrice;

public class NoneCondition implements DiscountCondition {
    @Override
    public boolean isCondition(final TotalPrice orderPrice) {
        return true;
    }

    @Override
    public long getMinimumPrice() {
        return 0;
    }
}

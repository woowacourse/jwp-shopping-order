package cart.domain.coupon.discountPolicy;

import cart.domain.TotalPrice;

public class PercentPolicy implements DiscountPolicy {

    @Override
    public TotalPrice discount(final TotalPrice orderPrice, final long value) {
        return orderPrice.discountPercent(value);
    }

    @Override
    public String getName() {
        return PolicyType.PERCENT.getName();
    }
}

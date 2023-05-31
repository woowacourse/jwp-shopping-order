package cart.domain.coupon.discountPolicy;

import cart.domain.Money;

public class PercentPolicy implements DiscountPolicy {

    @Override
    public Money discount(final Money orderPrice, final long value) {
        return orderPrice.percent(value);
    }

    @Override
    public String getName() {
        return PolicyType.PERCENT.getName();
    }
}

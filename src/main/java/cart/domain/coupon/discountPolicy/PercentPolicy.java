package cart.domain.coupon.discountPolicy;

import cart.domain.Money;

import java.math.BigDecimal;

public class PercentPolicy implements DiscountPolicy {

    @Override
    public Money discount(final Money orderPrice, final BigDecimal value) {
        return orderPrice.percent(value);
    }

    @Override
    public String getName() {
        return PolicyType.PERCENT.getName();
    }
}

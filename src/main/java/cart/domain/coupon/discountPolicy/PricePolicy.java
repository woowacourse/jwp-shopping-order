package cart.domain.coupon.discountPolicy;

import cart.domain.Money;

public class PricePolicy implements DiscountPolicy {

    @Override
    public Money discount(final Money orderPrice, final long value) {
        return new Money(value);
    }

    @Override
    public String getName() {
        return PolicyType.PRICE.getName();
    }
}

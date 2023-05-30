package cart.domain.coupon.discountPolicy;

import cart.domain.TotalPrice;

public class PricePolicy implements DiscountPolicy {

    @Override
    public TotalPrice discount(final TotalPrice orderPrice, final long value) {
        return orderPrice.discountAmount(value);
    }

    @Override
    public String getName() {
        return PolicyType.PRICE.getName();
    }
}

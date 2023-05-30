package cart.domain.coupon.discountPolicy;

import cart.domain.TotalPrice;

public interface DiscountPolicy {

    TotalPrice discount(final TotalPrice orderPrice, final long value);

    String getName();
}

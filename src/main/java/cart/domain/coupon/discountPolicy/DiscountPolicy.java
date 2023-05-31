package cart.domain.coupon.discountPolicy;

import cart.domain.Money;

public interface DiscountPolicy {

    Money discount(final Money money, final long value);

    String getName();
}

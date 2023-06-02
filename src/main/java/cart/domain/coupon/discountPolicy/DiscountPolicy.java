package cart.domain.coupon.discountPolicy;

import cart.domain.Money;

import java.math.BigDecimal;

public interface DiscountPolicy {

    Money discount(final Money money, final BigDecimal value);

    String getName();
}

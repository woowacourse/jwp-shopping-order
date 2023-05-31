package cart.domain.discountpolicy;

import cart.domain.Money;

public interface DiscountPolicy {

    Money apply(Money original, double value);
}

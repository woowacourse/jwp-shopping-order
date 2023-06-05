package cart.domain.coupon;

import cart.domain.VO.Money;

@FunctionalInterface
public interface CalculateFunction {

    Money apply(final Long discountValue, final Money price);
}

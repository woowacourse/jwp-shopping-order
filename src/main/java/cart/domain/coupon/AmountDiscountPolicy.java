package cart.domain.coupon;

import cart.domain.common.Money;

public class AmountDiscountPolicy implements DiscountPolicy {

    @Override
    public Money calculatePrice(final Long discountValue, final Money price) {
        final Money subtrahend = Money.from(discountValue);
        final Money result = price.minus(subtrahend);
        if (result.isGreaterThanOrEqual(Money.ZERO)) {
            return result;
        }
        return Money.ZERO;
    }

    @Override
    public Money calculateDeliveryFee(final Long discountValue, final Money deliveryFee) {
        return deliveryFee;
    }
}

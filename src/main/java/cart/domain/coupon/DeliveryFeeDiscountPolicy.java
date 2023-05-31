package cart.domain.coupon;

import cart.domain.common.Money;

public class DeliveryFeeDiscountPolicy implements DiscountPolicy {

    @Override
    public Money calculatePrice(final Long discountValue, final Money price) {
        return price;
    }

    @Override
    public Money calculateDeliveryFee(final Long discountValue, final Money deliveryFee) {
        final Money subtrahend = Money.from(discountValue);
        final Money result = deliveryFee.minus(subtrahend);
        if (result.isGreaterThanOrEqual(Money.ZERO)) {
            return result;
        }
        return Money.ZERO;
    }
}

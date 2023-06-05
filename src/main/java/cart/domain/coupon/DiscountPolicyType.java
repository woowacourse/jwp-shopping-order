package cart.domain.coupon;

import cart.domain.VO.Money;
import cart.exception.coupon.DiscountPolicyNotFoundException;
import java.util.Arrays;

public enum DiscountPolicyType {
    NONE(
            (discountValue, price) -> price,
            (discountValue, price) -> price
    ),
    PRICE(
            (discountValue, price) -> price.minus(Money.from(discountValue)),
            (discountValue, price) -> price
    ),
    PERCENT(
            (discountValue, price) -> price.minus(price.times(Double.valueOf(discountValue) / 100L)),
            (discountValue, price) -> price
    ),
    DELIVERY(
            (discountValue, price) -> price,
            (discountValue, price) -> price.minus(Money.from(discountValue))
    ),
    ;

    private final CalculateFunction discountValueCalculator;
    private final CalculateFunction deliveryFeeCalculator;

    DiscountPolicyType(final CalculateFunction discountValueCalculator, final CalculateFunction deliveryFeeCalculator) {
        this.discountValueCalculator = discountValueCalculator;
        this.deliveryFeeCalculator = deliveryFeeCalculator;
    }

    public static DiscountPolicyType from(final String name) {
        return Arrays.stream(values())
                .filter(discountPolicyType -> discountPolicyType.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(DiscountPolicyNotFoundException::new);
    }

    public Money calculateDiscountValue(final Long discountValue, final Money money) {
        return discount(discountValueCalculator, discountValue, money);
    }

    private Money discount(final CalculateFunction calculateFunction, final Long discountValue, final Money money) {
        if (discountValue == 0L) {
            return money;
        }
        final Money result = calculateFunction.apply(discountValue, money);
        if (Money.ZERO.isGreaterThanOrEqual(result)) {
            return Money.ZERO;
        }
        return result;
    }

    public Money calculateDeliveryFee(final Long discountValue, final Money money) {
        return discount(deliveryFeeCalculator, discountValue, money);
    }
}

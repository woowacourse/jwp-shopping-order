package cart.domain.coupon;

import cart.domain.common.Money;
import cart.exception.DiscountPolicyNotFoundException;
import java.util.Arrays;

public enum DiscountPolicyType {
    NONE(new NoneDiscountPolicy()),
    PRICE(new AmountDiscountPolicy()),
    PERCENT(new PercentDiscountPolicy()),
    DELIVERY(new DeliveryFeeDiscountPolicy()),
    ;

    private final DiscountPolicy discountPolicy;

    DiscountPolicyType(final DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public static DiscountPolicyType from(final String name) {
        return Arrays.stream(values())
                .filter(discountPolicyType -> discountPolicyType.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(DiscountPolicyNotFoundException::new);
    }

    public Money calculateDiscountValue(final Long discountValue, final Money money) {
        return discountPolicy.calculatePrice(discountValue, money);
    }

    public Money calculateDeliveryFee(final Long discountValue, final Money money) {
        return discountPolicy.calculateDeliveryFee(discountValue, money);
    }
}

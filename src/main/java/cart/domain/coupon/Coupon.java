package cart.domain.coupon;

import cart.domain.common.Money;

public class Coupon {

    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;
    private final DiscountCondition discountCondition;

    public Coupon(
            final Long id,
            final String name,
            final DiscountPolicy discountPolicy,
            final DiscountCondition discountCondition
    ) {
        this.id = id;
        this.name = name;
        this.discountPolicy = discountPolicy;
        this.discountCondition = discountCondition;
    }

    public Money calculatePrice(final Money totalPrice) {
        if (discountCondition.isSatisfiedBy(totalPrice)) {
            return discountPolicy.calculatePrice(totalPrice);
        }
        return totalPrice;
    }

    public Money calculateDeliveryFee(final Money totalPrice, final Money deliveryFee) {
        if (discountCondition.isSatisfiedBy(totalPrice)) {
            return discountPolicy.calculateDeliveryFee(deliveryFee);
        }
        return deliveryFee;
    }
}

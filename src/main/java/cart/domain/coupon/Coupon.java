package cart.domain.coupon;

import cart.domain.common.Money;

public class Coupon {

    public static final Coupon EMPTY = new Coupon(
            null,
            "",
            new AmountDiscountPolicy(0),
            new NoneDiscountCondition()
    );

    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;
    private final DiscountCondition discountCondition;

    public Coupon(final String name, final DiscountPolicy discountPolicy, final DiscountCondition discountCondition) {
        this(null, name, discountPolicy, discountCondition);
    }

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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public DiscountCondition getDiscountCondition() {
        return discountCondition;
    }
}

package cart.domain.coupon;

import static cart.domain.coupon.DiscountPolicyType.NONE;

import cart.domain.common.Money;

public class Coupon {

    public static final Coupon EMPTY = new Coupon("", NONE, 0L, Money.ZERO);

    private final Long id;
    private final String name;
    private final DiscountPolicyType discountPolicyType;
    private final Long discountValue;
    private final Money minimumPrice;

    public Coupon(
            final String name,
            final DiscountPolicyType discountPolicyType,
            final Long discountValue,
            final Money minimumPrice
    ) {
        this(null, name, discountPolicyType, discountValue, minimumPrice);
    }

    public Coupon(
            final Long id,
            final String name,
            final DiscountPolicyType discountPolicyType,
            final Long discountValue,
            final Money minimumPrice
    ) {
        this.id = id;
        this.name = name;
        this.discountPolicyType = discountPolicyType;
        this.discountValue = discountValue;
        this.minimumPrice = minimumPrice;
    }

    public boolean isInvalidPrice(final Money price) {
        return !price.isGreaterThanOrEqual(minimumPrice);
    }

    public Money calculatePrice(final Money price) {
        return discountPolicyType.calculateDiscountValue(discountValue, price);
    }

    public Money calculateDeliveryFee(final Money deliveryFee) {
        return discountPolicyType.calculateDeliveryFee(discountValue, deliveryFee);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DiscountPolicyType getDiscountPolicyType() {
        return discountPolicyType;
    }

    public Long getDiscountValue() {
        return discountValue;
    }

    public Money getMinimumPrice() {
        return minimumPrice;
    }

    public Long getMinimumPriceValue() {
        return minimumPrice.getLongValue();
    }
}

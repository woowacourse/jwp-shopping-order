package cart.domain.coupon;

import cart.domain.common.Money;

public class AmountDiscountPolicy implements DiscountPolicy {

    private final DiscountPolicyType discountPolicyType;
    private final Money discountPrice;

    public AmountDiscountPolicy(final long discountPrice) {
        this.discountPolicyType = DiscountPolicyType.PRICE;
        this.discountPrice = Money.from(discountPrice);
    }

    @Override
    public Money calculatePrice(final Money price) {
        return price.minus(discountPrice);
    }

    @Override
    public Money calculateDeliveryFee(final Money deliveryFee) {
        return deliveryFee;
    }

    @Override
    public DiscountPolicyType getDiscountPolicyType() {
        return discountPolicyType;
    }

    @Override
    public Money getDiscountPrice() {
        return discountPrice;
    }

    @Override
    public int getDiscountPercent() {
        return 0;
    }

    @Override
    public boolean isDiscountDeliveryFee() {
        return false;
    }
}

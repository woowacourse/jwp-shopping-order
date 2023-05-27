package cart.domain.coupon;

import cart.domain.common.Money;

public class DeliveryFeeDiscountPolicy implements DiscountPolicy {

    private final DiscountPolicyType discountPolicyType;

    public DeliveryFeeDiscountPolicy() {
        this.discountPolicyType = DiscountPolicyType.DELIVERY;
    }

    @Override
    public Money calculatePrice(final Money price) {
        return price;
    }

    @Override
    public Money calculateDeliveryFee(final Money deliveryFee) {
        return Money.ZERO;
    }

    @Override
    public DiscountPolicyType getDiscountPolicyType() {
        return discountPolicyType;
    }

    @Override
    public Money getDiscountPrice() {
        return Money.ZERO;
    }

    @Override
    public int getDiscountPercent() {
        return 0;
    }

    @Override
    public boolean isDiscountDeliveryFee() {
        return true;
    }
}

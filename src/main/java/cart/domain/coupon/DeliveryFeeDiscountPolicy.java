package cart.domain.coupon;

import cart.domain.common.Money;

public class DeliveryFeeDiscountPolicy implements DiscountPolicy {

    private final DiscountPolicyType discountPolicyType;
    private final boolean discountDeliveryFee;

    public DeliveryFeeDiscountPolicy(final boolean discountDeliveryFee) {
        this.discountPolicyType = DiscountPolicyType.DELIVERY;
        this.discountDeliveryFee = discountDeliveryFee;
    }

    @Override
    public Money calculatePrice(final Money price) {
        return price;
    }

    @Override
    public Money calculateDeliveryFee(final Money deliveryFee) {
        if (discountDeliveryFee) {
            return Money.ZERO;
        }
        return deliveryFee;
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
        return discountDeliveryFee;
    }
}

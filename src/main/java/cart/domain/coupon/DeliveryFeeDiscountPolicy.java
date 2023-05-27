package cart.domain.coupon;

public class DeliveryFeeDiscountPolicy implements DiscountPolicy {

    private final DiscountPolicyType discountPolicyType;
    private final boolean discountDeliveryFee;

    public DeliveryFeeDiscountPolicy() {
        this.discountPolicyType = DiscountPolicyType.DELIVERY;
        this.discountDeliveryFee = true;
    }

    @Override
    public DiscountPolicyType getDiscountPolicyType() {
        return discountPolicyType;
    }

    @Override
    public long getDiscountPrice() {
        return 0;
    }

    @Override
    public long getDiscountPercent() {
        return 0;
    }

    @Override
    public boolean isDiscountDeliveryFee() {
        return discountDeliveryFee;
    }
}

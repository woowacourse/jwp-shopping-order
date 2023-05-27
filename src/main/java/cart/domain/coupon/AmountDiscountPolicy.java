package cart.domain.coupon;

public class AmountDiscountPolicy implements DiscountPolicy {

    private final DiscountPolicyType discountPolicyType;
    private final long discountPrice;

    public AmountDiscountPolicy(final long discountPrice) {
        this.discountPolicyType = DiscountPolicyType.PRICE;
        this.discountPrice = discountPrice;
    }

    @Override
    public DiscountPolicyType getDiscountPolicyType() {
        return discountPolicyType;
    }

    @Override
    public long getDiscountPrice() {
        return discountPrice;
    }

    @Override
    public long getDiscountPercent() {
        return 0;
    }

    @Override
    public boolean isDiscountDeliveryFee() {
        return false;
    }
}

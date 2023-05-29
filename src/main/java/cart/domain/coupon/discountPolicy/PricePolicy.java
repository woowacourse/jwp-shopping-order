package cart.domain.coupon.discountPolicy;

import cart.domain.TotalPrice;

public class PricePolicy implements DiscountPolicy {

    private final long amount;

    public PricePolicy(final long amount) {
        this.amount = amount;
    }

    @Override
    public TotalPrice discount(final TotalPrice orderPrice) {
        return orderPrice.discountAmount(amount);
    }

    @Override
    public String getName() {
        return PolicyType.PRICE.getName();
    }

    @Override
    public long getDiscountPrice() {
        return amount;
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

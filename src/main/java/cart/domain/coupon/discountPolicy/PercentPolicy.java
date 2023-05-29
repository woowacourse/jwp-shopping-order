package cart.domain.coupon.discountPolicy;

import cart.domain.TotalPrice;

public class PercentPolicy implements DiscountPolicy {

    private final int percent;

    public PercentPolicy(final int percent) {
        this.percent = percent;
    }

    @Override
    public TotalPrice discount(final TotalPrice orderPrice) {
        return orderPrice.discountPercent(percent);
    }

    @Override
    public String getName() {
        return PolicyType.PERCENT.getName();
    }

    @Override
    public long getDiscountPrice() {
        return 0;
    }

    @Override
    public int getDiscountPercent() {
        return percent;
    }

    @Override
    public boolean isDiscountDeliveryFee() {
        return false;
    }
}

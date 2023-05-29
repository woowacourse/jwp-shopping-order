package cart.domain.coupon.discountPolicy;

import cart.domain.OrderPrice;

public class PercentPolicy implements DiscountPolicy {

    private final int percent;

    public PercentPolicy(final int percent) {
        this.percent = percent;
    }

    @Override
    public OrderPrice discount(final OrderPrice orderPrice) {
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

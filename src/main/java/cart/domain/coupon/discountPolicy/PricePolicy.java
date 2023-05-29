package cart.domain.coupon.discountPolicy;

import cart.domain.OrderPrice;

public class PricePolicy implements DiscountPolicy {

    private final long amount;

    public PricePolicy(final long amount) {
        this.amount = amount;
    }

    @Override
    public OrderPrice discount(final OrderPrice orderPrice) {
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

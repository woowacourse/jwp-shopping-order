package cart.domain.coupon.discountPolicy;

import cart.domain.TotalPrice;

public class DeliveryPolicy implements DiscountPolicy {

    @Override
    public TotalPrice discount(final TotalPrice orderPrice) {
        return orderPrice.discountDelivery();
    }

    @Override
    public String getName() {
        return PolicyType.DELIVERY.getName();
    }

    @Override
    public long getDiscountPrice() {
        return 0;
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

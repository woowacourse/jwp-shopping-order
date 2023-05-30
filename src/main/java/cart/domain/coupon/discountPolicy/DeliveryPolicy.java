package cart.domain.coupon.discountPolicy;

import cart.domain.TotalPrice;

public class DeliveryPolicy implements DiscountPolicy {

    @Override
    public TotalPrice discount(final TotalPrice orderPrice, final long value) {
        return orderPrice.discountDelivery(value);
    }

    @Override
    public String getName() {
        return PolicyType.DELIVERY.getName();
    }
}

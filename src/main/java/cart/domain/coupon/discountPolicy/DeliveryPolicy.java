package cart.domain.coupon.discountPolicy;

import cart.domain.Money;

public class DeliveryPolicy implements DiscountPolicy {

    @Override
    public Money discount(final Money deliveryFee, final long value) {
        return deliveryFee;
    }

    @Override
    public String getName() {
        return PolicyType.DELIVERY.getName();
    }
}

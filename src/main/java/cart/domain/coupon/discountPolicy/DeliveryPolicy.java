package cart.domain.coupon.discountPolicy;

import cart.domain.Money;

import java.math.BigDecimal;

public class DeliveryPolicy implements DiscountPolicy {

    @Override
    public Money discount(final Money deliveryFee, final BigDecimal value) {
        return deliveryFee;
    }

    @Override
    public String getName() {
        return PolicyType.DELIVERY.getName();
    }
}

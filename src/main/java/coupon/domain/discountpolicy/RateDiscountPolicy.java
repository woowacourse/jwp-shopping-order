package coupon.domain.discountpolicy;

import coupon.domain.Money;
import coupon.domain.Order;

public class RateDiscountPolicy implements DiscountPolicy {

    private final int discountRate;

    public RateDiscountPolicy(int discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public Money calculateDiscountAmount(Order order) {
        return order.getOriginalPrice().subtractAmountByPercentage(discountRate);
    }
}

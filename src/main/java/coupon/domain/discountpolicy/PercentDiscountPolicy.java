package coupon.domain.discountpolicy;

import coupon.domain.Money;
import coupon.domain.Order;

public class PercentDiscountPolicy implements DiscountPolicy {

    private final int discountRate;

    public PercentDiscountPolicy(int discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public Money calculateDiscountAmount(Order order) {
        return order.getOriginalPrice().getMoneyByPercentage(discountRate);
    }

    public int getDiscountRate() {
        return discountRate;
    }
}

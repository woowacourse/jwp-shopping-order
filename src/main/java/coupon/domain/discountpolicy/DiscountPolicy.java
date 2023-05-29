package coupon.domain.discountpolicy;

import coupon.domain.Money;
import coupon.domain.Order;

public interface DiscountPolicy {

    Money calculateDiscountAmount(Order order);
}

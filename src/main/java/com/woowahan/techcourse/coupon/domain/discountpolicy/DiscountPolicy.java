package com.woowahan.techcourse.coupon.domain.discountpolicy;

import com.woowahan.techcourse.coupon.domain.Money;
import com.woowahan.techcourse.coupon.domain.Order;

public interface DiscountPolicy {

    Money calculateDiscountAmount(Order order);

    int getAmount();

    String getName();
}

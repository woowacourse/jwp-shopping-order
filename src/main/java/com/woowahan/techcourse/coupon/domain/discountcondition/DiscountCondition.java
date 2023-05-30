package com.woowahan.techcourse.coupon.domain.discountcondition;

import com.woowahan.techcourse.coupon.domain.Order;

public interface DiscountCondition {

    boolean isSatisfiedBy(Order order);

    String getName();
}

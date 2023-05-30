package com.woowahan.techcourse.coupon.domain.discountcondition;

import com.woowahan.techcourse.coupon.domain.Order;

public class NoneDiscountCondition implements DiscountCondition {

    @Override
    public boolean isSatisfiedBy(Order order) {
        return false;
    }

    @Override
    public String getName() {
        return "NONE";
    }
}

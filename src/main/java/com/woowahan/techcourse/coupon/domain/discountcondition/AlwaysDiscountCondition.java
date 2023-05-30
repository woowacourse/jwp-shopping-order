package com.woowahan.techcourse.coupon.domain.discountcondition;

import com.woowahan.techcourse.coupon.domain.Order;

public class AlwaysDiscountCondition implements DiscountCondition {

    @Override
    public boolean isSatisfiedBy(Order order) {
        return true;
    }

    @Override
    public String getName() {
        return "ALWAYS";
    }
}

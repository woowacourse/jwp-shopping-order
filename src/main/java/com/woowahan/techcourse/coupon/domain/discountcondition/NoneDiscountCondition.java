package com.woowahan.techcourse.coupon.domain.discountcondition;

import com.woowahan.techcourse.coupon.domain.OriginalAmount;

public class NoneDiscountCondition implements DiscountCondition {

    @Override
    public boolean isSatisfiedBy(OriginalAmount originalAmount) {
        return false;
    }

    @Override
    public String getName() {
        return "NONE";
    }
}

package com.woowahan.techcourse.coupon.domain.discountcondition;

import com.woowahan.techcourse.coupon.domain.OriginalAmount;

public class AlwaysDiscountCondition implements DiscountCondition {

    @Override
    public boolean isSatisfiedBy(OriginalAmount originalAmount) {
        return true;
    }

    @Override
    public String getName() {
        return "ALWAYS";
    }
}

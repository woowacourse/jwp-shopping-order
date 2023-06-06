package com.woowahan.techcourse.coupon.domain.discountcondition;

import com.woowahan.techcourse.coupon.domain.OriginalAmount;

public interface DiscountCondition {

    boolean isSatisfiedBy(OriginalAmount originalAmount);

    String getName();
}

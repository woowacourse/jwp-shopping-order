package com.woowahan.techcourse.coupon.domain.discountpolicy;

import com.woowahan.techcourse.coupon.domain.Money;
import com.woowahan.techcourse.coupon.domain.OriginalAmount;

public interface DiscountPolicy {

    Money calculateDiscountAmount(OriginalAmount originalAmount);

    int getAmount();

    String getName();
}

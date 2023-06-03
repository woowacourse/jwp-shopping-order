package com.woowahan.techcourse.coupon.domain.discountpolicy;

import com.woowahan.techcourse.coupon.domain.Money;
import com.woowahan.techcourse.coupon.domain.OriginalAmount;

public class PercentDiscountPolicy implements DiscountPolicy {

    private final int discountRate;

    public PercentDiscountPolicy(int discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public Money calculateDiscountAmount(OriginalAmount originalAmount) {
        return originalAmount.getOriginalPrice().getMoneyByPercentage(discountRate);
    }

    @Override
    public int getAmount() {
        return discountRate;
    }

    @Override
    public String getName() {
        return "percent";
    }
}

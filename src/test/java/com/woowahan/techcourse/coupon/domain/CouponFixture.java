package com.woowahan.techcourse.coupon.domain;

import com.woowahan.techcourse.coupon.domain.discountcondition.AlwaysDiscountCondition;
import com.woowahan.techcourse.coupon.domain.discountpolicy.PercentDiscountPolicy;

public class CouponFixture {

    public static final Coupon COUPON1 = new Coupon(1L, "첫번재 쿠폰", new AlwaysDiscountCondition(),
            new PercentDiscountPolicy(10));
    public static final Coupon COUPON2 = new Coupon(2L, "두번재 쿠폰", new AlwaysDiscountCondition(),
            new PercentDiscountPolicy(20));
    public static final Coupon COUPON3 = new Coupon(3L, "세번재 쿠폰", new AlwaysDiscountCondition(),
            new PercentDiscountPolicy(30));


    private CouponFixture() {
    }
}

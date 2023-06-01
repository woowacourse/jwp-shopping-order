package com.woowahan.techcourse.coupon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowahan.techcourse.coupon.domain.discountcondition.AlwaysDiscountCondition;
import com.woowahan.techcourse.coupon.domain.discountcondition.DiscountCondition;
import com.woowahan.techcourse.coupon.domain.discountcondition.NoneDiscountCondition;
import com.woowahan.techcourse.coupon.domain.discountpolicy.DiscountPolicy;
import com.woowahan.techcourse.coupon.domain.discountpolicy.PercentDiscountPolicy;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class CouponTest {

    private final DiscountPolicy discountPolicy = new PercentDiscountPolicy(30);
    private final DiscountCondition alwaysDiscountCondition = new AlwaysDiscountCondition();
    private final DiscountCondition noneDiscountCondition = new NoneDiscountCondition();

    @Test
    void 쿠폰_이름을_반환한다() {
        // given
        Coupon coupon = new Coupon("쿠폰", alwaysDiscountCondition, discountPolicy);

        // when
        CouponName result = coupon.getName();

        // then
        assertThat(result).isEqualTo(new CouponName("쿠폰"));
    }

    @Test
    void 할인_조건이_만족되면_할인_금액을_반환한다() {
        // given
        Coupon coupon = new Coupon("쿠폰", alwaysDiscountCondition, discountPolicy);

        // when
        Money result = coupon.calculateDiscountAmount(new Order(10000));

        // then
        assertThat(result.getValue().doubleValue()).isEqualTo(new Money(3000).getValue().doubleValue());
    }

    @Test
    void 할인_조건이_만족되지_않으면_할인_금액을_반환한다() {
        // given
        Coupon coupon = new Coupon("쿠폰", noneDiscountCondition, discountPolicy);

        // when
        Money result = coupon.calculateDiscountAmount(new Order(10000));

        // then
        assertThat(result).isEqualTo(Money.ZERO);
    }
}

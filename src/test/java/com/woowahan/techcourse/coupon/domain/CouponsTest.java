package com.woowahan.techcourse.coupon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowahan.techcourse.coupon.domain.discountcondition.AlwaysDiscountCondition;
import com.woowahan.techcourse.coupon.domain.discountcondition.NoneDiscountCondition;
import com.woowahan.techcourse.coupon.domain.discountpolicy.PercentDiscountPolicy;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class CouponsTest {

    private static final Coupon 언제나_10_할인 = new Coupon("쿠폰1",
            new AlwaysDiscountCondition(),
            new PercentDiscountPolicy(10));
    private static final Coupon 언제나_20_할인 = new Coupon("쿠폰2",
            new NoneDiscountCondition(),
            new PercentDiscountPolicy(20));
    private static final Coupon 언제나_30_할인 = new Coupon("쿠폰3",
            new AlwaysDiscountCondition(),
            new PercentDiscountPolicy(30));
    private static final Coupon 언제나_100_할인 = new Coupon("쿠폰4",
            new AlwaysDiscountCondition(),
            new PercentDiscountPolicy(100));

    @Test
    void 할인_금액이_정상적으로_계산된다() {
        // given
        Coupons coupons = new Coupons(List.of(언제나_10_할인, 언제나_20_할인, 언제나_30_할인));

        // when
        Money result = coupons.calculateActualPrice(new OriginalAmount(10000));

        // then
        assertThat(result.getValue().doubleValue()).isEqualTo(new Money(6000).getValue().doubleValue());
    }

    @Test
    void 할인_금액이_총_금액을_넘어가면_0원이_된다() {
        // given
        Coupons coupons = new Coupons(List.of(언제나_10_할인, 언제나_20_할인, 언제나_30_할인, 언제나_100_할인));

        // when
        Money result = coupons.calculateActualPrice(new OriginalAmount(10000));

        // then
        assertThat(result).isEqualTo(Money.ZERO);
    }
}

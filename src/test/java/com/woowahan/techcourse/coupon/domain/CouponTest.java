package com.woowahan.techcourse.coupon.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowahan.techcourse.coupon.domain.discountcondition.AlwaysDiscountCondition;
import com.woowahan.techcourse.coupon.domain.discountcondition.DiscountCondition;
import com.woowahan.techcourse.coupon.domain.discountcondition.NoneDiscountCondition;
import com.woowahan.techcourse.coupon.domain.discountpolicy.DiscountPolicy;
import com.woowahan.techcourse.coupon.domain.discountpolicy.PercentDiscountPolicy;
import com.woowahan.techcourse.coupon.exception.CouponException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
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
        String result = coupon.getName();

        // then
        assertThat(result).isEqualTo("쿠폰");
    }

    @Test
    void 할인_조건이_만족되면_할인_금액을_반환한다() {
        // given
        Coupon coupon = new Coupon("쿠폰", alwaysDiscountCondition, discountPolicy);

        // when
        Money result = coupon.calculateDiscountAmount(new OriginalAmount(10000));

        // then
        assertThat(result.getValue().doubleValue()).isEqualTo(new Money(3000).getValue().doubleValue());
    }

    @Test
    void 할인_조건이_만족되지_않으면_할인_금액을_반환한다() {
        // given
        Coupon coupon = new Coupon("쿠폰", noneDiscountCondition, discountPolicy);

        // when
        Money result = coupon.calculateDiscountAmount(new OriginalAmount(10000));

        // then
        assertThat(result).isEqualTo(Money.ZERO);
    }

    @Nested
    class 쿠폰_이름을_검증한다 {

        @Test
        void 쿠폰_이름이_정상적으로_생성된다() {
            Coupon coupon = createCoupon("쿠폰 이름");

            assertThat(coupon.getName()).isEqualTo("쿠폰 이름");
        }

        @Test
        void 이름이_255자를_초과하면_예외가_발생한다() {
            String input = "쿠".repeat(256);

            assertThatThrownBy(() -> createCoupon(input))
                    .isInstanceOf(CouponException.class)
                    .hasMessageContaining("쿠폰 이름은 255자를 초과할 수 없습니다.");
        }

        private Coupon createCoupon(String name) {
            return new Coupon(name, alwaysDiscountCondition, discountPolicy);
        }
    }
}

package cart.coupon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Coupon 은(는)")
class CouponTest {

    @Test
    void 상품에_적용가능한지_판단한다() {
        // given
        Coupon 말랑이_멋있어서_주는_쿠폰 = new Coupon(
                "말랑이 멋있어서 주는 쿠폰",
                new FixDiscountPolicy(99999999),
                new GeneralCouponType(),
                1L);

        Long productId = 10L;

        // when & then
        assertThat(말랑이_멋있어서_주는_쿠폰.canApply(productId)).isTrue();
    }

    @Test
    void 가격을_할인한다() {
        // given
        Coupon 코코닥_불쌍해서_주는_쿠폰 = new Coupon(
                "코코닥 불쌍해서 주는 쿠폰",
                new FixDiscountPolicy(1000),
                new SpecificCouponType(1L),
                2L);

        // when & then
        assertThat(코코닥_불쌍해서_주는_쿠폰.apply(2000))
                .isEqualTo(1000);
    }
}

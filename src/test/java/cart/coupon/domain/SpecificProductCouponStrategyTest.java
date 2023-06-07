package cart.coupon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("SpecificProductCouponStrategy 은(는)")
class SpecificProductCouponStrategyTest {

    @Test
    void 특정_상품에_적용된다() {
        // given
        Long productId = 1L;
        CouponStrategy couponStrategy = new SpecificProductCouponStrategy(productId);

        // when & then
        assertThat(couponStrategy.canApply(productId)).isTrue();
        assertThat(couponStrategy.canApply(2L)).isFalse();
    }
}

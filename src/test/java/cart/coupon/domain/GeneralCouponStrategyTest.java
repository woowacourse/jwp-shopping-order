package cart.coupon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("GeneralCouponStrategy 은(는)")
class GeneralCouponStrategyTest {

    @Test
    void 무조건_적용된다() {
        // given
        CouponStrategy couponStrategy = new GeneralCouponStrategy();

        // when & then
        assertThat(couponStrategy.canApply(1L)).isTrue();
    }
}

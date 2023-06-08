package cart.coupon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("GeneralCouponType 은(는)")
class GeneralCouponTypeTest {

    @Test
    void 무조건_적용된다() {
        // given
        CouponType couponType = new GeneralCouponType();

        // when & then
        assertThat(couponType.canApply(1L)).isTrue();
    }
}

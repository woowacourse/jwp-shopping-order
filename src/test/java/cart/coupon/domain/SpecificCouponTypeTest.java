package cart.coupon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("SpecificCouponType 은(는)")
class SpecificCouponTypeTest {

    @Test
    void 특정_상품에_적용된다() {
        // given
        Long productId = 1L;
        CouponType couponType = new SpecificCouponType(productId);

        // when & then
        assertThat(couponType.canApply(productId)).isTrue();
        assertThat(couponType.canApply(2L)).isFalse();
    }
}

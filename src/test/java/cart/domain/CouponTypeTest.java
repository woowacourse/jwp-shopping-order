package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;


@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CouponTyp 은(는)")
class CouponTypeTest {

    @Test
    void 고정금액으로_천원_할인한다() {
        // when
        int actual = CouponType.FIXED.apply(4000, 1000);

        // then
        assertThat(actual).isEqualTo(3000);
    }


    @Test
    void 퍼센트로_10프로_할인한다() {
        // when
        int actual = CouponType.RATE.apply(4000, 10);

        // then
        assertThat(actual).isEqualTo(3600);
    }

    @Test
    void 최종_금액은_소숫점_첫재짜리를_내린다() {
        // when
        int actual = CouponType.RATE.apply(1004, 10);

        // then
        assertThat(actual).isEqualTo(903);
    }
}

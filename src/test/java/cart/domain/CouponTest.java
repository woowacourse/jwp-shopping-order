package cart.domain;


import static cart.domain.CouponType.PRICE;
import static cart.domain.CouponType.RATE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Coupon 은(는)")
class CouponTest {

    @Nested
    class 고정할인시 {

        @Test
        void 천원_할인한다() {
            // given
            Coupon coupon = new Coupon("1000원 할인 쿠폰", PRICE, 1000);

            // when
            int actual = coupon.apply(4000);

            // then
            assertThat(actual).isEqualTo(3000);
        }

        @Test
        void 쿠폰을_적용한_금액은_0미만으로_내려가지않는다() {
            // given
            Coupon coupon = new Coupon("1000원 할인 쿠폰", PRICE, 3000);

            // when
            int actual = coupon.apply(2000);

            // then
            assertThat(actual).isEqualTo(0);
        }
    }

    @Nested
    class 퍼센트할인시 {

        @Test
        void 최종_금액을_반환한다() {
            // given
            Coupon coupon = new Coupon("10% 할인 쿠폰", RATE, 10);

            // when
            int actual = coupon.apply(2000);

            // then
            assertThat(actual).isEqualTo(1800);
        }

        @Test
        void 최종_금액은_소숫점_첫재짜리를_내린다() {
            // given
            Coupon coupon = new Coupon("10% 할인 쿠폰", RATE, 10);

            // when
            int actual = coupon.apply(1004);

            // then
            assertThat(actual).isEqualTo(903);
        }
    }
}

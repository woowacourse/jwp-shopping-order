package coupon.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import coupon.exception.CouponException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class CouponNameTest {

    @Test
    void 쿠폰_이름이_정상적으로_생성된다() {
        CouponName couponName = new CouponName("쿠폰 이름");

        assertThat(couponName.getValue()).isEqualTo("쿠폰 이름");
    }

    @Test
    void 이름이_255자를_초과하면_예외가_발생한다() {
        String input = "쿠".repeat(256);

        assertThatThrownBy(() -> new CouponName(input))
                .isInstanceOf(CouponException.class)
                .hasMessageContaining("쿠폰 이름은 255자를 초과할 수 없습니다.");
    }

    @Test
    void 이름이_같으면_같은_쿠폰_이름이다() {
        CouponName couponName1 = new CouponName("쿠폰 이름");
        CouponName couponName2 = new CouponName("쿠폰 이름");

        assertThat(couponName1).isEqualTo(couponName2);
    }

    @Test
    void 이름이_다르면_다른_쿠폰_이름이다() {
        CouponName couponName1 = new CouponName("쿠폰 이름1");
        CouponName couponName2 = new CouponName("쿠폰 이름2");

        assertThat(couponName1).isNotEqualTo(couponName2);
    }
}

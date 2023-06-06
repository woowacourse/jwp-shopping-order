package cart.domain;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.exception.CouponException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static cart.fixture.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberCouponTest {

    @Test
    void 만료기간이_지난_쿠폰이면_예외가_발생한다() {
        LocalDate expiredDate = LocalDate.of(2012, 6, 16);
        MemberCoupon memberCoupon = new MemberCoupon(1L, 밀리, 쿠폰_10퍼센트, expiredDate);

        assertThatThrownBy(() -> memberCoupon.validate(밀리))
                .isInstanceOf(CouponException.class);
    }

    @Test
    void 쿠폰의_사용자가_다르면_예외가_발생한다() {
        MemberCoupon memberCoupon = new MemberCoupon(1L, 밀리, 쿠폰_10퍼센트, LocalDate.now());

        assertThatThrownBy(() -> memberCoupon.validate(박스터))
                .isInstanceOf(CouponException.class);
    }

    @Test
    void 없는_쿠폰이면_예외가_발생하지_않는다() {
        LocalDate expiredDate = LocalDate.of(2012, 6, 16);
        MemberCoupon memberCoupon = new MemberCoupon(1L, 밀리, Coupon.NONE, expiredDate);

        assertDoesNotThrow(() -> memberCoupon.validate(박스터));
    }

    @Test
    void 없는_쿠폰이면_false를_반환한다() {
        MemberCoupon memberCoupon = new MemberCoupon(1L, 밀리, Coupon.NONE, LocalDate.now());

        boolean result = memberCoupon.isExists();

        assertThat(result).isFalse();
    }

    @Test
    void 있는_쿠폰이면_true를_반환한다() {
        MemberCoupon memberCoupon = new MemberCoupon(1L, 밀리, 쿠폰_10퍼센트, LocalDate.now());

        boolean result = memberCoupon.isExists();

        assertThat(result).isTrue();
    }
}

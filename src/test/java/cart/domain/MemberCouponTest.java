package cart.domain;

import static cart.fixture.TestFixture.밀리;
import static cart.fixture.TestFixture.박스터;
import static cart.fixture.TestFixture.쿠폰_10퍼센트;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.domain.coupon.Coupon;
import cart.exception.IllegalCouponException;
import cart.exception.IllegalMemberException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberCouponTest {

    @Test
    void 만료기간이_지난_쿠폰이면_예외가_발생한다() {
        LocalDate expiredDate = LocalDate.of(2012, 6, 16);
        MemberCoupon memberCoupon = new MemberCoupon(1L, 밀리, 쿠폰_10퍼센트, expiredDate);

        assertThatThrownBy(() -> memberCoupon.check(밀리))
                .isInstanceOf(IllegalCouponException.class);
    }

    @Test
    void 쿠폰의_사용자가_다르면_예외가_발생한다() {
        MemberCoupon memberCoupon = new MemberCoupon(1L, 밀리, 쿠폰_10퍼센트, LocalDate.now());

        assertThatThrownBy(() -> memberCoupon.check(박스터))
                .isInstanceOf(IllegalMemberException.class);
    }

    @Test
    void 없는_쿠폰이면_예외가_발생하지_않는다() {
        LocalDate expiredDate = LocalDate.of(2012, 6, 16);
        MemberCoupon memberCoupon = new MemberCoupon(1L, 밀리, Coupon.NONE, expiredDate);

        assertDoesNotThrow(() -> memberCoupon.check(박스터));
    }
}

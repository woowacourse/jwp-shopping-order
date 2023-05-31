package cart.domain.member;

import cart.domain.fixture.Fixture;
import cart.exception.CouponAlreadyUsedException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberCouponTest {

    @Test
    void 이미_사용된_쿠폰을_사용하면_예외를_던진다() {
        // given
        final MemberCoupon memberCoupon = new MemberCoupon(Fixture.member1, Fixture.coupon1, true);

        // when & then
        assertThatThrownBy(memberCoupon::use)
                .isInstanceOf(CouponAlreadyUsedException.class);
    }

    @Test
    void 사용되지_않은_쿠폰을_사용하면_예외를_던지지_않는다() {
        // given
        final MemberCoupon memberCoupon = new MemberCoupon(Fixture.member1, Fixture.coupon1, false);

        // when & then
        assertThatCode(memberCoupon::use).doesNotThrowAnyException();
    }
}
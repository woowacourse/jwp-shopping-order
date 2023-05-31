package cart.domain;

import static cart.fixture.TestFixture.MEMBER_A_COUPON_FIXED_2000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import cart.exception.UsedCouponException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberCouponTest {

    @Test
    void 쿠폰을_사용해_할인한다() {
        var memberCoupon = MEMBER_A_COUPON_FIXED_2000;

        assertThat(memberCoupon.discount(new Money(12000))).isEqualTo(new Money(2000));
    }

    @Test
    void 사용자의_쿠폰은_다시_사용할_수_없다() {
        var memberCoupon = MEMBER_A_COUPON_FIXED_2000;

        memberCoupon.discount(new Money(12000));

        assertThatThrownBy(() -> memberCoupon.discount(new Money(13000)))
                .isInstanceOf(UsedCouponException.class);
    }
}

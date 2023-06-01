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
    void 사용자_쿠폰을_사용해_할인한다() {
        var memberCoupon = MEMBER_A_COUPON_FIXED_2000();

        Money price = new Money(12000);

        assertThat(memberCoupon.getDiscounted(price)).isEqualTo(new Money(10000));
    }

    @Test
    void 사용자_쿠폰은_다시_사용할_수_없다() {
        var memberCoupon = MEMBER_A_COUPON_FIXED_2000();

        memberCoupon.use();

        assertThatThrownBy(() -> memberCoupon.use())
                .isInstanceOf(UsedCouponException.class);
    }
}

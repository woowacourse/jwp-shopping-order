package cart.domain.cart;

import static cart.domain.VO.Money.from;
import static cart.fixture.CouponFixture._3만원_이상_2천원_할인_쿠폰;
import static cart.fixture.CouponFixture._3만원_이상_배달비_3천원_할인_쿠폰;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.VO.Money;
import cart.domain.coupon.Coupon;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberCouponTest {

    @Test
    void empty_메서드는_빈_쿠폰을_가진_MemberCoupon을_반환한다() {
        // expect
        final MemberCoupon memberCoupon = MemberCoupon.empty(1L);
        assertThat(memberCoupon.getCoupon()).isEqualTo(Coupon.EMPTY);
    }

    @Test
    void 사용할_수_없는_쿠폰인지_확인한다() {
        // given
        final MemberCoupon memberCoupon = new MemberCoupon(1L, _3만원_이상_2천원_할인_쿠폰);

        // expect
        assertThat(memberCoupon.isInvalidCoupon(from(29999L))).isTrue();
    }

    @Test
    void 총_금액을_받아_할인_후의_금액을_반환한다() {
        // given
        final MemberCoupon memberCoupon = new MemberCoupon(1L, _3만원_이상_2천원_할인_쿠폰);

        // when
        final Money result = memberCoupon.calculatePrice(Money.from(53000L));

        // then
        assertThat(result).isEqualTo(Money.from(51000L));
    }

    @Test
    void 배달비를_받아_할인_후의_배달비를_반환한다() {
        // given
        final MemberCoupon memberCoupon = new MemberCoupon(1L, _3만원_이상_배달비_3천원_할인_쿠폰);

        // when
        final Money result = memberCoupon.calculateDeliveryFee(Money.from(3000L));

        // then
        assertThat(result).isEqualTo(Money.ZERO);
    }

    @Test
    void 쿠폰을_사용_완료_상태로_만든다() {
        // given
        final MemberCoupon memberCoupon = new MemberCoupon(1L, _3만원_이상_배달비_3천원_할인_쿠폰);

        // when
        memberCoupon.use();

        // then
        assertThat(memberCoupon.isUsed()).isTrue();
    }
}

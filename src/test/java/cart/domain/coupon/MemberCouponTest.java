package cart.domain.coupon;

import cart.domain.Money;
import cart.exception.CouponException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static cart.domain.fixture.CouponFixture.AMOUNT_1000_COUPON;
import static cart.domain.fixture.CouponFixture.RATE_10_COUPON;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberCouponTest {

    @Test
    @DisplayName("사용하지 않은 쿠폰을 사용한다.")
    void use_unused_coupon_test() {
        // given
        MemberCoupon coupon = new MemberCoupon(RATE_10_COUPON);
        Money originalPrice = new Money(1000);

        // when
        Money couponUsedPrice = coupon.use(originalPrice);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(couponUsedPrice).isEqualTo(new Money(900));
            softly.assertThat(coupon.isUsed()).isTrue();
        });
    }

    @Test
    @DisplayName("이미 사용한 쿠폰을 사용한다.")
    void use_used_coupon_test() {
        // given
        MemberCoupon coupon = new MemberCoupon(RATE_10_COUPON);
        coupon.use(new Money(10000));

        // when
        // then
        assertThatThrownBy(() -> coupon.use(new Money(10000)))
                .isInstanceOf(CouponException.AlreadyUsed.class);
    }

    @Test
    @DisplayName("주문금액보다 큰 할인액의 쿠폰을 사용한다.")
    void use_coupon_over_order_price_test() {
        // given
        MemberCoupon coupon = new MemberCoupon(AMOUNT_1000_COUPON);
        Money originalPrice = new Money(900);

        // when
        // then
        assertThatThrownBy(() -> coupon.use(originalPrice))
                .isInstanceOf(CouponException.OverOriginalPrice.class);
    }
}

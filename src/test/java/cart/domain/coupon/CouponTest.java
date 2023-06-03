package cart.domain.coupon;

import cart.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static cart.domain.fixture.CouponFixture.AMOUNT_1000_COUPON;
import static cart.domain.fixture.CouponFixture.RATE_10_COUPON;
import static org.assertj.core.api.Assertions.assertThat;

class CouponTest {

    @Test
    @DisplayName("정량 할인 쿠폰을 적용한다.")
    void use_amount_discount_coupon_test() {
        // given
        Money originalPrice = new Money(10000);
        Coupon amountCoupon = AMOUNT_1000_COUPON;

        // when
        Money discountedPrice = amountCoupon.apply(originalPrice);

        //then
        assertThat(discountedPrice).isEqualTo(new Money(9000));
    }

    @Test
    @DisplayName("정률 할인 쿠폰을 적용한다.")
    void use_rate_discount_coupon_test() {
        // give
        Money originalPrice = new Money(10000);
        Coupon rateCoupon = RATE_10_COUPON;

        // when
        Money discountedPrice = rateCoupon.apply(originalPrice);

        // then
        assertThat(discountedPrice).isEqualTo(new Money(9000));
    }

    @Test
    @DisplayName("주문금액보다 더 큰 할인액의 쿠폰을 사용한다.")
    void use_coupon_over_order_price_test() {
        // given
        Money originalPrice = new Money(900);
        Coupon coupon = AMOUNT_1000_COUPON;

        // when
        Money usedPrice = coupon.apply(originalPrice);

        // then
        assertThat(usedPrice).isEqualTo(new Money(0));
    }
}

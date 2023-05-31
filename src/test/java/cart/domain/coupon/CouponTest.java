package cart.domain.coupon;

import cart.domain.Money;
import cart.domain.discountpolicy.AmountDiscountPolicy;
import cart.domain.discountpolicy.RateDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CouponTest {

    @Test
    @DisplayName("정량 할인 쿠폰을 적용한다.")
    void use_amount_discount_coupon_test() {
        // given
        Money originalPrice = new Money(10000);
        Coupon amountCoupon = new Coupon("1000원 할인 쿠폰", new AmountDiscountPolicy(), 1000);

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
        Coupon rateCoupon = new Coupon("10퍼센트 할인 쿠폰", new RateDiscountPolicy(), 0.9);

        // when
        Money discountedPrice = rateCoupon.apply(originalPrice);

        // then
        assertThat(discountedPrice).isEqualTo(new Money(9000));
    }

}

package cart.domain.coupon;

import cart.domain.Money;
import cart.domain.coupon.discountPolicy.DeliveryPolicy;
import cart.domain.coupon.discountPolicy.PricePolicy;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class CouponTest {

    @Test
    void 최소_주문_금액을_만족하면_할인금액을_할인한다() {
        // given
        final Coupon coupon = new Coupon(1L, "50000원 이상 5000원 할인 쿠폰", new PricePolicy(), 5000L, new Money(50000));

        // when
        final Money discount = coupon.discount(new Money(50000L));

        // then
        assertThat(discount.getValue()).isEqualTo(5000);
    }

    @Test
    void 최소_주문_금액을_만족하지_않으면_할인_금액은_0원이다() {
        // given
        final Coupon coupon = new Coupon(1L, "50000원 이상 5000원 할인 쿠폰", new PricePolicy(), 5000L, new Money(50000));

        // when
        final Money discount = coupon.discount(new Money(49999L));

        // then
        assertThat(discount.getValue()).isEqualTo(0);
    }

    @Test
    void 최소_주문_금액을_만족하면_배달비를_할인한다() {
        // given
        final Coupon coupon = new Coupon(1L, "30000원 이상 배달비 무료 쿠폰", new DeliveryPolicy(), 0, new Money(30000L));

        // when
        final Money discount = coupon.discountDeliveryFee(new Money(30000L), new Money(3000L));

        // then
        assertThat(discount.getValue()).isEqualTo(3000L);
    }

    @Test
    void 최소_주문_금액을_만족하지_않으면_배달비를_할인하지_않는다() {
        // given
        final Coupon coupon = new Coupon(1L, "30000원 이상 배달비 무료 쿠폰", new DeliveryPolicy(), 0, new Money(30000L));

        // when
        final Money discount = coupon.discountDeliveryFee(new Money(29999L), new Money(3000L));

        // then
        assertThat(discount.getValue()).isEqualTo(0L);
    }
}
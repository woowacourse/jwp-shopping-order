package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CouponTest {

    @DisplayName("주어진 금액에 대해 쿠폰으로 할인한 값을 반환한다.")
    @Test
    void discount() {
        // given
        final Coupon coupon = new Coupon(1L,
                new CouponType(1L, "배송비 쿠폰", DiscountType.DISCOUNT_PRICE, new Money(3000L)), false);
        // when
        final Money price = new Money(4000L);

        // then
        assertThat(coupon.discount(price)).isEqualTo(new Money(1000L));
    }

    @DisplayName("쿠폰을 사용 상태로 변경한다.")
    @Test
    void use() {
        // given
        final Coupon coupon = new Coupon(1L,
                new CouponType(1L, "배송비 쿠폰", DiscountType.DISCOUNT_PRICE, new Money(3000L)), false);

        // when
        final Coupon used = coupon.use();

        // then
        assertThat(used.isUsed()).isTrue();
    }

    @DisplayName("쿠폰을 미사용 상태로 변경한다.")
    @Test
    void refund() {
        // given
        final Coupon coupon = new Coupon(1L,
                new CouponType(1L, "배송비 쿠폰", DiscountType.DISCOUNT_PRICE, new Money(3000L)), true);

        // when
        final Coupon used = coupon.refund();

        // then
        assertThat(used.isUsed()).isFalse();
    }
}

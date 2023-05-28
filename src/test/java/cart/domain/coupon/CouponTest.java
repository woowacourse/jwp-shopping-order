package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.common.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CouponTest {

    @Test
    void 총_금액을_받아_할인_후_금액을_반환한다() {
        // given
        final Coupon coupon = new Coupon(
                1L,
                "2000원 할인 쿠폰",
                new AmountDiscountPolicy(2000L),
                new NoneDiscountCondition()
        );

        // when
        final Money result = coupon.calculatePrice(Money.from(53000));

        // then
        assertThat(result).isEqualTo(Money.from(51000));
    }

    @Test
    void 배달비를_받아_할인_후_배달비를_반환한다() {
        // given
        final Coupon coupon = new Coupon(
                1L,
                "배달비 할인 쿠폰",
                new DeliveryFeeDiscountPolicy(),
                new MinimumPriceDiscountCondition(30000)
        );

        // when
        final Money result = coupon.calculateDeliveryFee(Money.from(30000), Money.from(3000));

        // then
        assertThat(result).isEqualTo(Money.ZERO);
    }
}

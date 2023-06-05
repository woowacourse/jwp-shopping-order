package cart.domain.coupon;

import static cart.domain.coupon.DiscountPolicyType.DELIVERY;
import static cart.domain.coupon.DiscountPolicyType.PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.VO.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CouponTest {

    @Test
    void 입력받은_금액이_최소_주문_금액을_만족하는지_확인한다() {
        // given
        final Coupon coupon = new Coupon(1L, "배달비", DELIVERY, 3000L, Money.from(30000L), false, 1L);

        // when
        final boolean result = coupon.isInvalidPrice(Money.from(29999L));

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 총_금액을_받아_할인_후_금액을_반환한다() {
        // given
        final Coupon coupon = new Coupon(1L, "2000원 할인", PRICE, 2000L, Money.from(30000L), false, 1L);

        // when
        final Money result = coupon.calculatePrice(Money.from(53000L));

        // then
        assertThat(result).isEqualTo(Money.from(51000L));
    }

    @Test
    void 배달비를_받아_할인_후_배달비를_반환한다() {
        // given
        final Coupon coupon = new Coupon(1L, "배달비", DELIVERY, 3000L, Money.from(30000L), false, 1L);

        // when
        final Money result = coupon.calculateDeliveryFee(Money.from(3000L));

        // then
        assertThat(result).isEqualTo(Money.ZERO);
    }
}

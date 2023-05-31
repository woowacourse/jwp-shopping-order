package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.VO.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AmountDiscountPolicyTest {

    private final DiscountPolicy discountPolicy = new AmountDiscountPolicy();

    @Test
    void 고정된_금액을_할인하여_반환한다() {
        // given
        final Money price = Money.from(30000L);

        // when
        final Money result = discountPolicy.calculatePrice(3000L, price);

        // then
        assertThat(result).isEqualTo(Money.from(27000L));
    }

    @Test
    void 배달비는_할인하지_않고_그대로_반환한다() {
        // given
        final Money deliveryFee = Money.from(3000L);

        // when
        final Money result = discountPolicy.calculateDeliveryFee(0L, deliveryFee);

        // then
        assertThat(result).isEqualTo(Money.from(3000L));
    }
}

package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.common.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DeliveryFeeDiscountPolicyTest {

    private final DiscountPolicy discountPolicy = new DeliveryFeeDiscountPolicy();

    @Test
    void 금액은_할인하지_않고_반환한다() {
        // given
        final Money price = Money.from(30000);

        // when
        final Money result = discountPolicy.calculatePrice(0L, price);

        // then
        assertThat(result).isEqualTo(Money.from(30000));
    }

    @Test
    void 배달비를_전부_할인하여_반환한다() {
        // given
        final Money deliveryFee = Money.from(3000);

        // when
        final Money result = discountPolicy.calculateDeliveryFee(3000L, deliveryFee);

        // then
        assertThat(result).isEqualTo(Money.from(0));
    }
}

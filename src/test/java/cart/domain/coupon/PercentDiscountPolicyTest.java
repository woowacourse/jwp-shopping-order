package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.VO.Money;
import cart.exception.coupon.InvalidDiscountPolicyException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PercentDiscountPolicyTest {

    private final DiscountPolicy discountPolicy = new PercentDiscountPolicy();

    @ValueSource(longs = {-1L, 101L})
    @ParameterizedTest(name = "{displayName} 입력값: {0}")
    void _0_에서_100_사이의_값을_입력받지_않으면_예외를_던진다(final long discountPercent) {
        // given
        final Money price = Money.from(30000L);

        // expect
        assertThatThrownBy(() -> discountPolicy.calculatePrice(discountPercent, price))
                .isInstanceOf(InvalidDiscountPolicyException.class)
                .hasMessage("비율 할인 정책의 비율은 0 ~ 100 사이여야합니다.");
    }

    @Test
    void 할인_비율에_해당하는_금액을_할인하여_반환한다() {
        // given
        final Money price = Money.from(30000);

        // when
        final Money result = discountPolicy.calculatePrice(30L, price);

        // then
        assertThat(result).isEqualTo(Money.from(21000));
    }

    @Test
    void 배달비는_할인하지_않고_그대로_반환한다() {
        // given
        final Money deliveryFee = Money.from(3000L);

        // when
        final Money result = discountPolicy.calculateDeliveryFee(3000L, deliveryFee);

        // then
        assertThat(result).isEqualTo(Money.from(3000));
    }
}

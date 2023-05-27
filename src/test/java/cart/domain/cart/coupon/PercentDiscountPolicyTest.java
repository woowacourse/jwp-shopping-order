package cart.domain.cart.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.common.Money;
import cart.domain.coupon.DiscountPolicy;
import cart.domain.coupon.DiscountPolicyType;
import cart.domain.coupon.PercentDiscountPolicy;
import cart.exception.InvalidDiscountPolicyException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PercentDiscountPolicyTest {

    @ValueSource(ints = {-1, 101})
    @ParameterizedTest(name = "{displayName} 입력값: {0}")
    void _0_에서_100_사이의_값을_입력받지_않으면_예외를_던진다(final int discountPercent) {
        // expect
        assertThatThrownBy(() -> new PercentDiscountPolicy(discountPercent))
                .isInstanceOf(InvalidDiscountPolicyException.class)
                .hasMessage("비율 할인 정책의 비율은 0 ~ 100 사이여야합니다.");
    }

    @ValueSource(ints = {0, 100})
    @ParameterizedTest(name = "{displayName} 입력값: {0}")
    void _0_에서_100_사이의_값을_입력받으면_정상_생성된다(final int discountPercent) {
        // expect
        final PercentDiscountPolicy discountPolicy = new PercentDiscountPolicy(discountPercent);
        assertThat(discountPolicy.getDiscountPercent()).isEqualTo(discountPercent);
    }

    @Test
    void 할인_비율을_제외하고_0이나_false를_반환한다() {
        // given
        final DiscountPolicy discountPolicy = new PercentDiscountPolicy(30);

        // expect
        assertAll(
                () -> assertThat(discountPolicy.getDiscountPrice()).isEqualTo(Money.ZERO),
                () -> assertThat(discountPolicy.isDiscountDeliveryFee()).isFalse()
        );
    }

    @Test
    void 할인_정책_PERCENT_타입을_반환한다() {
        // given
        final DiscountPolicy discountPolicy = new PercentDiscountPolicy(30);

        // expect
        assertThat(discountPolicy.getDiscountPolicyType()).isEqualTo(DiscountPolicyType.PERCENT);
    }

    @Test
    void 할인_비율에_해당하는_금액을_할인하여_반환한다() {
        // given
        final DiscountPolicy amountDiscountPolicy = new PercentDiscountPolicy(20);

        // when
        final Money result = amountDiscountPolicy.calculatePrice(Money.from(30000));

        // then
        assertThat(result).isEqualTo(Money.from(24000));
    }

    @Test
    void 배달비는_할인하지_않고_그대로_반환한다() {
        // given
        final DiscountPolicy amountDiscountPolicy = new PercentDiscountPolicy(20);

        // when
        final Money result = amountDiscountPolicy.calculateDeliveryFee(Money.from(3000));

        // then
        assertThat(result).isEqualTo(Money.from(3000));
    }
}

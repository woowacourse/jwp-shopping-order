package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        final PercentDiscountPolicy percentDiscountPolicy = new PercentDiscountPolicy(discountPercent);
        assertThat(percentDiscountPolicy.getDiscountPercent()).isEqualTo(discountPercent);
    }

    @Test
    void 할인_비율을_제외하고_0이나_false를_반환한다() {
        // given
        final DiscountPolicy percentDiscountPolicy = new PercentDiscountPolicy(30);

        // expect
        assertAll(
                () -> assertThat(percentDiscountPolicy.getDiscountPrice()).isZero(),
                () -> assertThat(percentDiscountPolicy.isDiscountDeliveryFee()).isFalse()
        );
    }

    @Test
    void 할인_정책_PERCENT_타입을_반환한다() {
        // given
        final DiscountPolicy percentDiscountPolicy = new PercentDiscountPolicy(30);

        // expect
        assertThat(percentDiscountPolicy.getDiscountPolicyType()).isEqualTo(DiscountPolicyType.PERCENT);
    }
}

package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Money;
import cart.domain.coupon.policy.DiscountPolicy;
import cart.domain.coupon.policy.FixedDiscountPolicy;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FixedDiscountPolicyTest {

    @Test
    void 입력받은_값으로_할인된_금액을_반환한다() {
        // given
        DiscountPolicy fixedDiscountPolicy = new FixedDiscountPolicy();

        // when
        Money result = fixedDiscountPolicy.discount(new Money(10000), BigDecimal.valueOf(3000));

        // then
        assertThat(result).isEqualTo(new Money(7000));
    }

    @Test
    void 입력받은_값이_할인할_가격보다_크면_0원을_반환한다() {
        // given
        DiscountPolicy fixedDiscountPolicy = new FixedDiscountPolicy();

        // when
        Money result = fixedDiscountPolicy.discount(new Money(10000), BigDecimal.valueOf(13000));

        // then
        assertThat(result).isEqualTo(new Money(0));
    }

    @Test
    void 입력받은_값이_0보다_크면_true를_반환한다() {
        // given
        DiscountPolicy fixedDiscountPolicy = new FixedDiscountPolicy();

        // when
        boolean result = fixedDiscountPolicy.isValid(BigDecimal.ONE);

        // then
        assertThat(result).isTrue();
    }

    @ValueSource(ints = {0, -1})
    @ParameterizedTest
    void 입력받은_값이_0이하이면_false를_반환한다(int value) {
        // given
        DiscountPolicy fixedDiscountPolicy = new FixedDiscountPolicy();

        // when
        boolean result = fixedDiscountPolicy.isValid(BigDecimal.valueOf(value));

        // then
        assertThat(result).isFalse();
    }
}

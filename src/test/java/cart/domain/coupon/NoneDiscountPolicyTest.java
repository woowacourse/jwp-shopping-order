package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Money;
import cart.domain.coupon.policy.DiscountPolicy;
import cart.domain.coupon.policy.NoneDiscountPolicy;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NoneDiscountPolicyTest {

    @Test
    void 할인하지_않은_금액을_반환한다() {
        // given
        DiscountPolicy noneDiscountPolicy = new NoneDiscountPolicy();

        // when
        Money result = noneDiscountPolicy.discount(new Money(100000), BigDecimal.ZERO);

        // then
        assertThat(result).isEqualTo(new Money(100000));
    }

    @Test
    void 입력받은_값이_0이면_true를_반환한다() {
        // given
        DiscountPolicy noneDiscountPolicy = new NoneDiscountPolicy();

        // when
        boolean result = noneDiscountPolicy.isValid(BigDecimal.ZERO);

        // then
        assertThat(result).isTrue();
    }

    @ValueSource(ints = {-1, 1})
    @ParameterizedTest
    void 입력받은_값이_0이_아니면_false를_반환한다(int value) {
        // given
        DiscountPolicy noneDiscountPolicy = new NoneDiscountPolicy();

        // when
        boolean result = noneDiscountPolicy.isValid(BigDecimal.valueOf(value));

        // then
        assertThat(result).isFalse();
    }
}

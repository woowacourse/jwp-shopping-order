package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Money;
import cart.domain.coupon.discountpolicy.DiscountPolicy;
import cart.domain.coupon.discountpolicy.RateDiscountPolicy;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RateDiscountPolicyTest {

    @Test
    void 입력받은_비율로_할인한_가격을_반환한다() {
        // given
        DiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();

        // when
        Money result = rateDiscountPolicy.discount(new Money(100000), BigDecimal.valueOf(15));

        // then
        assertThat(result).isEqualTo(new Money(85000));
    }

    @ValueSource(ints = {1, 40, 100})
    @ParameterizedTest
    void 입력받은_값이_0보다_크고_100이하이면_true를_반환한다(int value) {
        // given
        DiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();

        // when
        boolean result = rateDiscountPolicy.isValid(BigDecimal.valueOf(value));

        // then
        assertThat(result).isTrue();
    }

    @ValueSource(ints = {-1, 0, 101})
    @ParameterizedTest
    void 입력받은_값이_0이하_100초과이면_false를_반환한다(int value) {
        // given
        DiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();

        // when
        boolean result = rateDiscountPolicy.isValid(BigDecimal.valueOf(value));

        // then
        assertThat(result).isFalse();
    }
}

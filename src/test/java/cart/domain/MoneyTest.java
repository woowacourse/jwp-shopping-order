package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.InvalidMoneyException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MoneyTest {

    @ValueSource(ints = {-1, -100})
    @ParameterizedTest
    void 금액이_0보다_작으면_예외가_발생한다(int value) {
        // expect
        assertThatThrownBy(() -> new Money(value))
                .isInstanceOf(InvalidMoneyException.class)
                .hasMessage("금액은 양의 정수이어야 합니다.");
    }

    @CsvSource(value = {"0,10", "100, 110", "100000000, 100000010"})
    @ParameterizedTest
    void 금액의_합을_구한다(int value, int expect) {
        // given
        Money money = new Money(10);

        // when
        Money result = money.add(new Money(value));

        // then
        assertThat(result).isEqualTo(new Money(expect));
    }

    @CsvSource(value = {"0, 0", "100, 1000", "100000000, 1000000000"})
    @ParameterizedTest
    void 금액의_곱을_구한다(int value, int expect) {
        // given
        Money money = new Money(10);

        // when
        Money result = money.multiply(value);

        // then
        assertThat(result).isEqualTo(new Money(expect));
    }

    @CsvSource(value = {"0, 0, false", "100, 1000, true"})
    @ParameterizedTest
    void 금액의_값이_다른지_확인한다(int value, int otherValue, boolean expect) {
        // given
        Money money = new Money(value);

        // when
        boolean result = money.isNotSameValue(BigDecimal.valueOf(otherValue));

        // then
        assertThat(result).isEqualTo(expect);
    }
}

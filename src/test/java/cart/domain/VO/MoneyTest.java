package cart.domain.VO;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MoneyTest {

    @Test
    void _10000원에서_2000원을_더하면_12000원이다() {
        // given
        final Money money = Money.from(10000);

        // when
        final Money result = money.plus(Money.from(2000));

        // then
        assertThat(result).isEqualTo(Money.from(12000));
    }

    @Test
    void _10000원에서_2000원을_빼면_8000원이다() {
        // given
        final Money money = Money.from(10000);

        // when
        final Money result = money.minus(Money.from(2000));

        // then
        assertThat(result).isEqualTo(Money.from(8000));
    }

    @Test
    void _10000원의_10퍼센트는_1000원이다() {
        // given
        final Money money = Money.from(10000);
        final double ratio = 0.1;

        // when
        final Money result = money.times(ratio);

        // then
        assertThat(result).isEqualTo(Money.from(1000));
    }

    @Test
    void ZERO는_0원을_의미한다() {
        // given
        final Money zero = Money.ZERO;

        // expect
        assertThat(zero).isEqualTo(Money.from(0));
    }

    @Test
    void BigDecimal_타입의_값을_반환한다() {
        // given
        final Money money = Money.from(10000);

        // when
        final BigDecimal result = money.getValue();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(10000L));
    }

    @Test
    void Long_타입의_값을_반환한다() {
        // given
        final Money money = Money.from(10000);

        // when
        final long result = money.getLongValue();

        // then
        assertThat(result).isEqualTo(10000L);
    }

    @ParameterizedTest(name = "{0}원은 10000원 이상이다 = {1}")
    @CsvSource(value = {"10000, true", "10001, true", "9999, false"})
    void 입력받는_금액과_비교하여_입력받은_금액_이상인지_확인한다(final Long amount, final boolean returnValue) {
        // given
        final Money minAmount = Money.from(10000);
        final Money price = Money.from(amount);

        // when
        final boolean result = price.isGreaterThanOrEqual(minAmount);

        // then
        assertThat(result).isEqualTo(returnValue);
    }
}

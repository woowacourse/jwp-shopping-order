package cart.domain.VO;

import static cart.domain.VO.Money.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void null을_입력받으면_IllegalArugmentException을_던진다() {
        // expect
        assertThatThrownBy(() -> Money.from(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Money의 값은 null일 수 없습니다.");
    }

    @Test
    void _10000원에서_2000원을_더하면_12000원이다() {
        // given
        final Money money = from(10000L);

        // when
        final Money result = money.plus(from(2000L));

        // then
        assertThat(result).isEqualTo(from(12000L));
    }

    @Test
    void _10000원에서_2000원을_빼면_8000원이다() {
        // given
        final Money money = from(10000L);

        // when
        final Money result = money.minus(from(2000L));

        // then
        assertThat(result).isEqualTo(from(8000L));
    }

    @Test
    void _10000원의_10퍼센트는_1000원이다() {
        // given
        final Money money = from(10000L);
        final double ratio = 0.1;

        // when
        final Money result = money.times(ratio);

        // then
        assertThat(result).isEqualTo(from(1000L));
    }

    @Test
    void ZERO는_0원을_의미한다() {
        // given
        final Money zero = Money.ZERO;

        // expect
        assertThat(zero).isEqualTo(from(0L));
    }

    @Test
    void BigDecimal_타입의_값을_반환한다() {
        // given
        final Money money = from(10000L);

        // when
        final BigDecimal result = money.getValue();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(10000L));
    }

    @Test
    void Long_타입의_값을_반환한다() {
        // given
        final Money money = from(10000L);

        // when
        final long result = money.getLongValue();

        // then
        assertThat(result).isEqualTo(10000L);
    }

    @ParameterizedTest(name = "{0}원은 10000원 이상이다 = {1}")
    @CsvSource(value = {"10000, true", "10001, true", "9999, false"})
    void 입력받는_금액과_비교하여_입력받은_금액_이상인지_확인한다(final Long amount, final boolean returnValue) {
        // given
        final Money minAmount = from(10000L);
        final Money price = from(amount);

        // when
        final boolean result = price.isGreaterThanOrEqual(minAmount);

        // then
        assertThat(result).isEqualTo(returnValue);
    }
}

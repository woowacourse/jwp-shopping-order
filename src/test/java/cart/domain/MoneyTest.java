package cart.domain;

import cart.exception.InvalidMoneyValueException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MoneyTest {

    @Test
    void 돈은_음수가_될_수_없다() {
        assertThatThrownBy(() -> new Money(BigDecimal.valueOf(-1))).isInstanceOf(InvalidMoneyValueException.class);
    }

    @Test
    void 돈은_0이_될_수_있다() {
        assertThatCode(() -> new Money(BigDecimal.valueOf(0))).doesNotThrowAnyException();
    }

    @Test
    void 돈을_더한다() {
        // given
        final Money money1 = new Money(BigDecimal.valueOf(1000L));
        final Money money2 = new Money(BigDecimal.valueOf(2000L));

        // when
        final Money result = money1.sum(money2);

        // then
        assertThat(result.getValue().longValue()).isEqualTo(3000L);
    }

    @Test
    void 돈을_곱한다() {
        // given
        final Money money1 = new Money(BigDecimal.valueOf(1000L));
        final Money money2 = new Money(BigDecimal.valueOf(2000L));

        // when
        final Money result = money1.sum(money2);

        // then
        assertThat(result.getValue().longValue()).isEqualTo(3000L);
    }

    @Test
    void 백분율을_구한다() {
        // given
        final Money money = new Money(BigDecimal.valueOf(2000L));

        // when
        final Money result = money.percent(BigDecimal.valueOf(10));

        // then
        assertThat(result.getValue().longValue()).isEqualTo(200L);
    }

    @Test
    void 돈의_값이_이상인지_확인한다() {
        // given
        final Money money = new Money(BigDecimal.valueOf(2000L));

        // when
        final boolean result1 = money.isMoreThan(new Money(BigDecimal.valueOf(2000L)));
        final boolean result2 = money.isMoreThan(new Money(BigDecimal.valueOf(2001L)));

        // then
        assertAll(
                () -> assertThat(result1).isTrue(),
                () -> assertThat(result2).isFalse()
        );
    }

    @Test
    void 돈의_값이_미만인지_확인한다() {
        // given
        final Money money = new Money(BigDecimal.valueOf(2000L));

        // when
        final boolean result1 = money.isUnder(2001L);
        final boolean result2 = money.isUnder(2000L);

        // then
        assertAll(
                () -> assertThat(result1).isTrue(),
                () -> assertThat(result2).isFalse()
        );
    }
}

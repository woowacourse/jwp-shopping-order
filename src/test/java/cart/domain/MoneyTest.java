package cart.domain;

import cart.domain.common.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MoneyTest {

    @Test
    void 정상적으로_생성된다() {
        //given
        final int money = 1000;

        //expect
        assertThatNoException().isThrownBy(() -> Money.valueOf(money));
    }

    @Test
    void 돈이_0미만이면_예외를_던진다() {
        final int money = -1;

        //expect
        assertThatThrownBy(() -> Money.valueOf(money))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("돈은 0 미만일 수 없습니다.");
    }

    @Test
    void 돈을_더한다() {
        //given
        final Money money = Money.valueOf(1000);

        //when
        final Money addedMoney = money.add(Money.valueOf(100));

        //then
        assertThat(addedMoney).isEqualTo(Money.valueOf(1100));
    }

    @Test
    void 돈을_뺀다() {
        //given
        final Money money = Money.valueOf(1000);

        //when
        final Money subtractedMoney = money.subtract(Money.valueOf(100));

        //then
        assertThat(subtractedMoney).isEqualTo(Money.valueOf(900));
    }

    @ParameterizedTest
    @CsvSource({"999, true", "1000, true", "1001, false"})
    void 입력한_돈_이상인지_확인한다(final int amount, final boolean expected) {
        //given
        final Money money = Money.valueOf(1000);

        //when
        final boolean actual = money.isMoreThan(Money.valueOf(amount));

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 일정_비율의_돈을_얻는다() {
        //given
        final Money money = Money.valueOf(1000);
        final double rate = 0.1;

        //when
        final Money partialMoney = money.getPartial(rate);

        //then
        assertThat(partialMoney).isEqualTo(Money.valueOf(100));
    }
}

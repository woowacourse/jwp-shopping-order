package cart.domain;

import cart.domain.common.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

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
    void 뺄셈한다() {
        //given
        final Money money = Money.valueOf(1000);

        //when
        final Money subtractedMoney = money.subtract(Money.valueOf(100));

        //then
        assertThat(subtractedMoney).isEqualTo(Money.valueOf(900));
    }
}

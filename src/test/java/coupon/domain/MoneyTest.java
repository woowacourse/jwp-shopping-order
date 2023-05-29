package coupon.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
class MoneyTest {

    @ParameterizedTest
    @ValueSource(longs = {0, 1, 100, 1000, 10000})
    void 금액이_0원_이상이면_생성에_성공한다(long value) {
        // given
        // when
        Money money = new Money(value);

        // then
        assertSoftly(softly -> {
            softly.assertThat(money).isNotNull();
            softly.assertThat(money.getValue()).isEqualTo(value);
        });
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, -100, -1000, -10000})
    void 금액이_0원_미만이면_생성에_실패한다(long value) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Money(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("금액은 0원 이상이어야 합니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"0, 0", "1, 100", "10, 1000", "100, 10000"})
    void 금액의_비율에_해당하는_금액을_반환한다(int input, int expected) {
        // given
        Money money = new Money(10000);

        // when
        Money result = money.getMoneyByPercentage(input);

        // then
        assertThat(result.getValue()).isEqualTo(expected);
    }

    @Test
    void 금액_비율만큼_금액을_감한다() {
        // given
        Money money = new Money(10000);

        // when
        Money result = money.subtractAmountByPercentage(10);

        // then
        assertThat(result.getValue()).isEqualTo(9000);
    }

    @Test
    void 같은_금액이면_같은_객체다() {
        // given
        Money money1 = new Money(10000);
        Money money2 = new Money(10000);

        // when
        // then
        assertThat(money1).isEqualTo(money2);
    }

    @Test
    void 다른_금액이면_다른_객체다() {
        // given
        Money money1 = new Money(10000);
        Money money2 = new Money(20000);

        // when
        // then
        assertThat(money1).isNotEqualTo(money2);
    }
}

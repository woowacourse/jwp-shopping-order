package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.MoneyException.MultiplyZeroOrNegative;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class MoneyTest {

    Money defaultMoney = new Money(100);

    @Test
    @DisplayName("add를 호출하면 기존 Money에 지정한 Money를 더한 결과를 반환한다.")
    void addSuccessTest() {
        Money other = new Money(100);
        Money expected = new Money(200);

        Money actual = defaultMoney.add(other);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("subtract를 호출하면 기존 Money에 지정한 Money를 뺀 결과를 반환한다.")
    void subtractSuccessTest() {
        Money other = new Money(50);
        Money expected = new Money(50);

        Money actual = defaultMoney.subtract(other);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("multiply를 호출하면 기존 Money에 지정한 int amount를 곱한 결과를 반환한다.")
    void multiplySuccessTestWithIntParameter() {
        Money expected = new Money(300);

        Money actual = defaultMoney.multiply(3);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("multiply를 호출하면 기존 Money에 {0}인 Money를 지정한 경우 예외가 발생한다.")
    void multiplyFailTestWithIntParameter(int amount) {
        assertThatThrownBy(() -> defaultMoney.multiply(amount))
                .isInstanceOf(MultiplyZeroOrNegative.class)
                .hasMessage("금액에 0 이하의 값을 곱할 수 없습니다.");
    }

    @Test
    @DisplayName("multiply를 호출하면 기존 Money에 지정한 int amount를 곱한 결과를 반환한다.")
    void multiplySuccessTestWithDoubleParameter() {
        Money expected = new Money(30);

        Money actual = defaultMoney.multiply(0.3d);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0d, -1.0d})
    @DisplayName("multiply를 호출하면 기존 Money에 {0}인 Money를 지정한 경우 예외가 발생한다.")
    void multiplyFailTest(double amount) {
        assertThatThrownBy(() -> defaultMoney.multiply(amount))
                .isInstanceOf(MultiplyZeroOrNegative.class)
                .hasMessage("금액에 0 이하의 값을 곱할 수 없습니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {
            "99:true",
            "100:false",
            "101:false"
    }, delimiter = ':')
    @DisplayName("isGreaterThan을 호출하면 Money.100인 경우 {0}을 지정하면 {1}을 반환한다.")
    void isGreaterThanTest(int target, boolean expected) {
        Money targetMoney = new Money(target);

        boolean actual = defaultMoney.isGreaterThan(targetMoney);

        assertThat(actual).isEqualTo(expected);
    }
}

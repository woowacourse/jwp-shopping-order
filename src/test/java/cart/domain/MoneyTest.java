package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cart.exception.IllegalPercentageException;
import cart.exception.NegativeMoneyException;

public class MoneyTest {

    @DisplayName("0 이상이다")
    @Test
    void negative_money_throws() {
        assertThatThrownBy(() -> new Money(-1))
                .isInstanceOf(NegativeMoneyException.class);
    }

    @DisplayName("더할 수 있다")
    @Test
    void plus() {
        var money = new Money(1000);

        assertThat(money.plus(new Money(2000))).isEqualTo(new Money(3000));
    }

    @DisplayName("뺄 수 있다")
    @Test
    void minus() {
        var money = new Money(2000);

        assertThat(money.minus(new Money(1500))).isEqualTo(new Money(500));
    }

    @DisplayName("빼면 음수인지 알 수 있다")
    @Test
    void is_negative_by_subtracting_money() {
        var money = new Money(2000);

        assertThat(money.isNegativeBySubtracting(2001)).isTrue();
    }

    @DisplayName("백분율을 구할 수 있다")
    @Test
    void percentage_of_money() {
        var money = new Money(2000);

        assertThat(money.percentageOf(20)).isEqualTo(new Money(400));
    }

    @DisplayName("백분율한 금액의 소수점 이하는 버린다")
    @Test
    void calculate_percentage_by_down() {
        var money = new Money(2123);

        assertThat(money.percentageOf(23)).isEqualTo(new Money(488));
    }

    @DisplayName("백분율은 100이상 0이하이다")
    @Test
    void percentage_between_zero_and_hundred() {
        assertThatThrownBy(() -> new Money(15461).percentageOf(-1))
                .isInstanceOf(IllegalPercentageException.class);
        assertThatThrownBy(() -> new Money(15461).percentageOf(101))
                .isInstanceOf(IllegalPercentageException.class);
    }

    @DisplayName("변경해도, 기존 돈은 변하지 않는다.")
    @Test
    void calculation_does_not_affect_money() {
        Money original = new Money(12000);

        original.minus(new Money(10000));

        assertThat(original).isEqualTo(new Money(12000));
    }

    @DisplayName("곱할 수 있다")
    @Test
    void multiply() {
        Money original = new Money(12000);

        assertThat(original.multiply(3)).isEqualTo(new Money(36000));
    }
}

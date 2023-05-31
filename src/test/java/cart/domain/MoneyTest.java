package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoneyTest {
    @Test
    @DisplayName("값을 음수로 생성한다.")
    void createByNegativeValue() {
        //given
        //when
        //then
        assertThatThrownBy(() -> new Money(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("값이 같은 다른 객체와 동등성을 비교한다.")
    void equalsByValue() {
        //given
        int value = 10_000;
        Money money = new Money(value);
        //when
        boolean result = money.equals(new Money(value));
        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("다른 Money 객체를 더하면 값이 더해진 Money 객체를 반환하다.")
    void add() {
        //given
        Money base = new Money(3000);
        Money additive = new Money(1500);
        Money expected = new Money(4500);

        //when
        Money actual = base.add(additive);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("곱하기를 하면 원래 값에 비율을 곱한 Money 객체를 반환한다.")
    void multiply() {
        //given
        Money base = new Money(10_000);
        Money expected = new Money(1_000);
        //when
        Money actual = base.multiply(0.1);
        //then
        assertThat(actual).isEqualTo(expected);
    }
}
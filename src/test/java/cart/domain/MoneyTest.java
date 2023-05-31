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
}
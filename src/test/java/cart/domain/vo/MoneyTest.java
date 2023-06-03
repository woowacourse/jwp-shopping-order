package cart.domain.vo;

import static cart.domain.vo.Money.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MoneyTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 1000})
    @DisplayName("Money 생성 (성공)")
    void createMoney_success(int input) {
        Money money = from(input);

        assertThat(money.getValue()).isEqualTo(input);
    }

    @Test
    @DisplayName("Money 생성 (실패)")
    void createMoney_fail() {
        assertThatThrownBy(() -> Money.from(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
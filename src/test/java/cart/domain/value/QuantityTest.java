package cart.domain.value;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class QuantityTest {

    @DisplayName("수량이 0이하이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    void validateRange(int input) {
        // when, then
        assertThatThrownBy(() -> new Quantity(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 0이하일 수 없습니다.");
    }
}

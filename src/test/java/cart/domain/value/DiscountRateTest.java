package cart.domain.value;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DiscountRateTest {

    @DisplayName("할인율은 0이상 100이하가 아니면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 101})
    void validateRange(int input) {
        // when, then
        assertThatThrownBy(() -> new DiscountRate(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("허용되지 않는 할인율입니다.");
    }
}

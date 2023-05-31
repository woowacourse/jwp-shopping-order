package cart.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.IllegalQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuantityTest {

    @Test
    @DisplayName("수량이 1보다 작으면 예외가 발생한다.")
    void createQuantity_fail() {
        // when, then
        assertThatThrownBy(() -> Quantity.from(0))
            .isInstanceOf(IllegalQuantityException.class);
    }
}
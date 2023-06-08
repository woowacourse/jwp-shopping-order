package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.illegalexception.IllegalQuantityException;
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

    @Test
    @DisplayName("같은 값을 가진 수량이라면 동등성이 보장된다.")
    void sameQuantity() {
        // given
        Quantity quantity1 = Quantity.from(1);
        Quantity quantity2 = Quantity.from(1);

        // when, then
        assertThat(quantity1).isEqualTo(quantity2);
    }
}

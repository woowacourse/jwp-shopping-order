package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Quantity 단위 테스트")
class QuantityTest {

    @Test
    @DisplayName("뺄셈에 성공한다.")
    void subtract_success() {
        // given
        final Quantity quantity = new Quantity(50);
        final Quantity subtraction = new Quantity(10);

        // when
        final Quantity subtractedQuantity = quantity.subtract(subtraction);

        // then
        final Quantity expected = new Quantity(40);
        assertThat(subtractedQuantity).isEqualTo(expected);
    }
}

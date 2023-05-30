package cart.domain.cart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuantityTest {

    private static final int DEFAULT_QUANTITY = 1;

    @DisplayName("최소 수량은 1 이상이다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void quantityTest(final int value) {
        final Quantity quantity = new Quantity(value);
        assertThat(quantity.getValue()).isEqualTo(value);
    }

    @DisplayName("최소 수량인 1보다 작을 경우 Exception이 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2})
    void invalidQuantityTest(final int value) {
        assertThrows(IllegalArgumentException.class, () -> new Quantity(value));
    }

    @DisplayName("수량을 업데이트한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void updateQuantityTest(final int value) {
        final Quantity quantity = new Quantity(DEFAULT_QUANTITY);
        quantity.updateQuantity(value);
        assertThat(quantity.getValue()).isEqualTo(value);
    }

    @DisplayName("업데이트 수량이 1보다 작을 경우 Exception이 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2})
    void invalidUpdateQuantityTest(final int value) {
        final Quantity quantity = new Quantity(DEFAULT_QUANTITY);
        assertThrows(IllegalArgumentException.class, () -> quantity.updateQuantity(value));
    }
}

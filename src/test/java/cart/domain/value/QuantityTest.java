package cart.domain.value;

import cart.exception.value.quantity.InvalidQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class QuantityTest {

    @ParameterizedTest(name = "{displayName}")
    @ValueSource(ints = {0, -1, -100, -1000})
    @DisplayName("올바르지 않은 상품 수량({0})이면 에러를 발생한다.")
    void check_quantity(int quantity) {
        // when + then
        assertThatThrownBy(() -> new Quantity(quantity))
                .isInstanceOf(InvalidQuantityException.class);
    }
}

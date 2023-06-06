package cart.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PriceTest {

    @DisplayName("최소 가격은 1이다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void validPriceTest(final int value) {
        final Price price = new Price(value);
        assertThat(price.getValue()).isEqualTo(value);
    }

    @DisplayName("최소 가격인 1보다 작을 경우 Exception이 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2})
    void invalidPriceTest(final int value) {
        assertThrows(IllegalArgumentException.class, () -> new Price(value));
    }
}

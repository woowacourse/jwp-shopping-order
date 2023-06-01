package cart.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.NegativePriceException;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void constructWithNegativeValueThrowException() {
        assertThatThrownBy(() -> Price.valueOf(-1))
                .isInstanceOf(NegativePriceException.class);
    }

    @Test
    void constructWithZero() {
        assertThatCode(() -> Price.valueOf(0))
                .doesNotThrowAnyException();
    }
}

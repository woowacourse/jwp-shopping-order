package cart.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.NegativeStockException;
import org.junit.jupiter.api.Test;

class StockTest {

    @Test
    void constructWithNegativeValueThrowException() {
        assertThatThrownBy(() -> new Stock(-1))
                .isInstanceOf(NegativeStockException.class);
    }

    @Test
    void constructWithZero() {
        assertThatCode(() -> new Stock(0))
                .doesNotThrowAnyException();
    }
}

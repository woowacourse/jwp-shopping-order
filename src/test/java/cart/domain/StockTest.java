package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.NegativeStockException;
import cart.exception.NotEnoughStockException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @Test
    void reduceByOrderQuantityFail() {
        final Stock stock = new Stock(10);
        assertThatThrownBy(() -> stock.reduceByOrderQuantity(11))
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage(new NotEnoughStockException(10, 11).getMessage());
    }

    @ParameterizedTest
    @CsvSource({"10, 0", "9, 1"})
    void reduceByOrderQuantity(final int orderQuantity, final int expected) {
        final Stock stock = new Stock(10);
        final Stock result = stock.reduceByOrderQuantity(orderQuantity);
        assertThat(result).isEqualTo(new Stock(expected));
    }
}

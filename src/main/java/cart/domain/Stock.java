package cart.domain;

import java.util.Objects;

import cart.exception.NegativeStockException;
import cart.exception.NotEnoughStockException;

public class Stock {

    private static final int MINIMUM_STOCK = 0;

    private final int value;

    public Stock(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < MINIMUM_STOCK) {
            throw new NegativeStockException();
        }
    }

    public Stock reduceByOrderQuantity(final int orderQuantity) {
        validateOrderQuantity(orderQuantity);
        return new Stock(value - orderQuantity);
    }

    private void validateOrderQuantity(final int orderQuantity) {
        if (value < orderQuantity) {
            throw new NotEnoughStockException(value, orderQuantity);
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Stock stock = (Stock) o;
        return value == stock.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

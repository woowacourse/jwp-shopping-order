package cart.domain;

import cart.exception.NegativeStockException;

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

    public int getValue() {
        return value;
    }
}

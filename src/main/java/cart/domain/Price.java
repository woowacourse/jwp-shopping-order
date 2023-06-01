package cart.domain;

import java.util.Objects;

import cart.exception.NegativePriceException;

public class Price {

    private static final int MINIMUM_PRICE = 0;

    private final int value;

    private Price(final int value) {
        validate(value);
        this.value = value;
    }

    public static Price valueOf(final int value) {
        return new Price(value);
    }

    private void validate(final int value) {
        if (value < MINIMUM_PRICE) {
            throw new NegativePriceException();
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
        final Price price = (Price) o;
        return value == price.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

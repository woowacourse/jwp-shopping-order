package cart.domain;

import cart.exception.NotEnoughQuantityException;

import java.util.Objects;

public final class Quantity {

    private static final int BOUNDARY_VALUE = 0;

    private final int value;

    public Quantity(final int value) {
        validateNotNegative(value);
        this.value = value;
    }

    public Quantity subtract(final Quantity quantity) {
        return new Quantity(value - quantity.value);
    }

    private void validateNotNegative(final int value) {
        if (value < BOUNDARY_VALUE) {
            throw new NotEnoughQuantityException();
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantity)) return false;
        Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

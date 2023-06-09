package cart.domain;

import cart.exception.CartItemException;
import cart.exception.ErrorMessage;
import java.util.Objects;

public class Quantity {
    private final int value;

    public Quantity(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < 0) {
            throw new CartItemException(ErrorMessage.INVALID_QUANTITY);
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
        Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

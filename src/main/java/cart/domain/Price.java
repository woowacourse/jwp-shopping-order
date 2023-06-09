package cart.domain;

import cart.exception.ErrorMessage;
import cart.exception.ProductException;
import java.util.Objects;

public class Price {
    private final int value;

    public Price(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < 0) {
            throw new ProductException(ErrorMessage.INVALID_PRICE);
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
        Price price = (Price) o;
        return value == price.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public boolean isLessThan(final int value) {
        return this.value < value;
    }
}

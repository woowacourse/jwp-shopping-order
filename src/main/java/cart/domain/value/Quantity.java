package cart.domain.value;

import java.util.Objects;

public class Quantity {

    private final int value;

    public Quantity(final int value) {
        validateRange(value);
        this.value = value;
    }

    private void validateRange(final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("수량은 0이하일 수 없습니다.");
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
        final Quantity quantity = (Quantity) o;
        return getValue() == quantity.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}

package cart.domain;

import java.util.Objects;

public class Quantity {
    private final int value;

    public Quantity(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quantity quantity = (Quantity) o;
        return getValue() == quantity.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}

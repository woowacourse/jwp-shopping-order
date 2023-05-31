package cart.domain;

import java.util.Objects;

public class Quantity {

    private final int value;

    private Quantity(final int value) {
        this.value = value;
    }

    public static Quantity from(int value) {
        validate(value);
        return new Quantity(value);
    }

    private static void validate(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("수량은 0 미만으로 떨어질 수 없습니다.");
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
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}

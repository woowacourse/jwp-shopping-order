package cart.domain.vo;

import java.util.Objects;

public class Quantity {

    private final int value;

    private Quantity(int value) {
        this.value = value;
    }

    public static Quantity from(int value) {
        return new Quantity(value);
    }

    public Quantity increase(Quantity other) {
        return new Quantity(value + other.value);
    }

    public Quantity decrease(Quantity other) {
        return new Quantity(value - other.value);
    }

    public boolean isZero() {
        return value == 0;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Quantity{" +
                "value=" + value +
                '}';
    }
}

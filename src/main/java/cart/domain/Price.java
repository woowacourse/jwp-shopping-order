package cart.domain;

import java.util.Objects;

public class Price {
    public static final Price ZERO_PRICE = new Price(0);

    private final int value;

    public Price(final int value) {
        this.value = value;
    }

    public boolean lessThan(Price other) {
        return this.value < other.value;
    }

    public Price sum(Price other) {
        return new Price(this.value + other.value);
    }
    public Price minus(Price other) {
        return new Price(this.value - other.value);
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
}

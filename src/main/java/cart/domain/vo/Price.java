package cart.domain.vo;

import java.util.Objects;

public class Price {

    private final Integer value;

    public Price(final Integer value) {
        this.value = value;
    }

    public Price sum(final Price price) {
        return new Price(value + price.value);
    }

    public Price subtract(final Price other) {
        return new Price(value - other.value);
    }

    public Price multi(final Integer value) {
        return new Price(value * this.value);
    }

    public Price divide(final Integer value) {
        return new Price(this.value / value);
    }

    public Integer getValue() {
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
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Price{" +
                "value=" + value +
                '}';
    }
}

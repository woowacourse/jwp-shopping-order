package cart.domain;

import java.util.Objects;

public class Price {

    private final Integer value;

    public Price(final Integer value) {
        this.value = value;
    }

    public Price divide(final Integer divider) {
        return new Price(value / divider);
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
}

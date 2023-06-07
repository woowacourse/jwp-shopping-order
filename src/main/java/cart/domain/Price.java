package cart.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal value;

    public Price(final BigDecimal value) {
        this.value = value;
    }

    public Price(final int value) {
        this(new BigDecimal(value));
    }

    public Price calculateTotalPrice(final int quantity) {
        int result = value.multiply(new BigDecimal(quantity)).intValue();

        return new Price(result);
    }

    public Price add(final Price other) {
        return new Price(value.add(other.value));
    }

    public int getValue() {
        return value.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

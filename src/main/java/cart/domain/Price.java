package cart.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
    private final BigDecimal amount;

    private Price(BigDecimal amount) {
        this.amount = amount;
    }

    public Price from(long amount) {
        return new Price(BigDecimal.valueOf(amount));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        return Objects.equals(amount, price.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}

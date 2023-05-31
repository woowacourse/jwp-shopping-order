package cart.domain;

import cart.exception.InvalidMoneyException;
import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    private final BigDecimal value;

    public Money(int value) {
        this(BigDecimal.valueOf(value));
    }

    public Money(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(BigDecimal value) {
        if (value.compareTo(BigDecimal.valueOf(0)) == -1) {
            throw new InvalidMoneyException();
        }
    }

    public BigDecimal getValue() {
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
        Money money = (Money) o;
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public Money multiply(int multiplicand) {
        BigDecimal result = value.multiply(BigDecimal.valueOf(multiplicand));
        return new Money(result);
    }
}

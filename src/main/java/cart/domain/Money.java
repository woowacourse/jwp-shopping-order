package cart.domain;

import cart.exception.InvalidMoneyValueException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {

    private final BigDecimal value;

    public Money(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    public Money(final long value) {
        this(BigDecimal.valueOf(value));
    }

    private void validate(final BigDecimal value) {
        if (value.signum() == -1) {
            throw new InvalidMoneyValueException();
        }
    }

    public Money sum(final Money other) {
        return new Money(value.add(other.getValue()));
    }

    public Money mul(final int value) {
        return new Money(this.value.multiply(BigDecimal.valueOf(value)));
    }

    public Money percent(final BigDecimal percent) {
        return new Money(this.value.multiply(percent).divide(BigDecimal.valueOf(100), RoundingMode.CEILING));
    }

    public boolean isMoreThan(final Money other) {
        return this.value.compareTo(other.getValue()) >= 0;
    }

    public boolean isUnder(final long value) {
        return this.value.compareTo(BigDecimal.valueOf(value)) < 0;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Money money = (Money) o;
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

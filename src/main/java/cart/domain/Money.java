package cart.domain;

import cart.exception.InvalidMoneyValueException;

import java.util.Objects;

public class Money {

    private final long value;

    public Money(final long value) {
        validate(value);
        this.value = value;
    }

    private void validate(final long value) {
        if (value < 0) {
            throw new InvalidMoneyValueException();
        }
    }

    public Money sum(final Money other) {
        return new Money(value + other.getValue());
    }

    public Money mul(final int value) {
        return new Money(this.value * value);
    }

    public Money percent(final long percent) {
        return new Money(this.value * percent / 100);
    }

    public boolean isMoreThan(final Money other) {
        return this.value >= other.getValue();
    }

    public boolean isUnder(final long value) {
        return this.value < value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Money money = (Money) o;
        return value == money.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

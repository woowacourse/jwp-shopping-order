package cart.domain;

import cart.exception.MoneyException;
import java.util.Objects;

public class Money {

    private final long value;

    public Money(final long value) {
        validate(value);
        this.value = value;
    }

    private void validate(final long value) {
        if (value < 0) {
            throw new MoneyException.IllegalValue(value);
        }
    }

    public Money minus(final Money money) {
        return new Money(value - money.value);
    }

    public Money plus(final Money money) {
        return new Money(value + money.value);
    }

    public long getValue() {
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
        final Money money = (Money) o;
        return value == money.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Money{" +
                "value=" + value +
                '}';
    }
}

package cart.domain;

import cart.exception.IllegalMoneyAmountException;

import java.util.Objects;

public class Money {

    private final int value;

    public Money(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < 0) {
            throw new IllegalMoneyAmountException();
        }
    }

    public Money plus(Money money) {
        return new Money(this.value + money.value);
    }

    public Money subtract(Money money) {
        return new Money(this.value - money.value);
    }

    public int getValue() {
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

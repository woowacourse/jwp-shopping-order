package cart.domain;

import java.util.Objects;

public class Money {

    private final int value;

    public Money(int value) {
        validate(value);
        this.value = value;
    }

    public static Money from(int value) {
        return new Money(value);
    }

    private void validate(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("");
        }
    }

    public Money add(Money money) {
        return new Money(this.value + money.value);
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
        return value == money.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public int getValue() {
        return value;
    }

    public Money multiply(double rate) {
        return new Money((int) (this.value * rate));
    }

    public Money subtract(Money subtractive) {
        return new Money(this.value - subtractive.value);
    }

    @Override
    public String toString() {
        return "Money{" +
            "value=" + value +
            '}';
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return this.value >= other.value;
    }
}

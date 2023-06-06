package cart.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {
    private final int value;

    public Money(final int value) {
        this.validate(value);
        this.value = value;
    }

    public static Money from(final int value) {
        return new Money(value);
    }

    private void validate(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("");
        }
    }

    public Money add(final Money money) {
        return new Money(this.value + money.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Money money = (Money) o;
        return this.value == money.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    public int getValue() {
        return this.value;
    }

    public Money multiply(final BigDecimal rate) {
        final BigDecimal multiply = BigDecimal.valueOf(this.value).multiply(rate);
        return new Money(multiply.intValue());
    }

    public Money subtract(final Money subtractive) {
        return new Money(this.value - subtractive.value);
    }

    @Override
    public String toString() {
        return "Money{" +
                "value=" + this.value +
                '}';
    }

    public boolean isGreaterThanOrEqual(final Money other) {
        return this.value >= other.value;
    }
}

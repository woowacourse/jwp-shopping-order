package cart.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cart.exception.IllegalPercentageException;
import cart.exception.NegativeMoneyException;

public class Money {

    private static final int MINIMUM = 0;
    private static final int MAX_PERCENTAGE = 100;

    public static final Money ZERO = new Money(0);

    private final int value;

    public Money(int value) {
        validateNotNegative(value);
        this.value = value;
    }

    private void validateNotNegative(int value) {
        if (value < MINIMUM) {
            throw new NegativeMoneyException();
        }
    }

    public Money plus(Money other) {
        return new Money(value + other.value);
    }

    public Money minus(Money other) {
        return new Money(value - other.value);
    }

    public boolean isNegativeBySubtracting(int value) {
        return this.value - value < MINIMUM;
    }

    public Money multiply(int count) {
        return new Money(value * count);
    }

    public Money percentageOf(int percentage) {
        if (percentage < 0 || MAX_PERCENTAGE < percentage) {
            throw new IllegalPercentageException();
        }
        return new Money(valueOf(percentage));
    }

    private int valueOf(int percentage) {
        return BigDecimal.valueOf(value)
                .multiply(BigDecimal.valueOf(percentage))
                .divide(BigDecimal.valueOf(MAX_PERCENTAGE), RoundingMode.DOWN)
                .intValue();
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Money money = (Money)o;

        return value == money.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "Money{" +
                "value=" + value +
                '}';
    }
}

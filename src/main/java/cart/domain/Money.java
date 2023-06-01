package cart.domain;

import cart.exception.MoneyException;
import cart.exception.MoneyException.MultiplyZeroOrNegative;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {

    private static final int SCALE = 0;

    private final BigDecimal value;

    private Money(BigDecimal value) {
        this.value = value;
    }

    public Money(int value) {
        validateValue(value);
        this.value = new BigDecimal(value);
    }

    private void validateValue(int value) {
        if (value < 0) {
            throw new MoneyException.Negative();
        }
    }

    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        return new Money(this.value.subtract(other.value));
    }

    public Money multiply(int amount) {
        validateAmount(amount);
        BigDecimal targetAmount = new BigDecimal(amount);

        return new Money(this.value.multiply(targetAmount));
    }

    private void validateAmount(int amount) {
        if (amount <= 0) {
            throw new MultiplyZeroOrNegative();
        }
    }

    public Money multiply(double amount) {
        validateAmount(amount);
        BigDecimal divisor = BigDecimal.valueOf(amount);

        return new Money(value.multiply(divisor).setScale(SCALE, RoundingMode.DOWN));
    }

    private void validateAmount(double amount) {
        if (amount <= 0.0d) {
            throw new MultiplyZeroOrNegative();
        }
    }

    public boolean isGreaterThan(Money other) {
        return this.value.compareTo(other.value) > 0;
    }

    public int getValue() {
        return value.intValue();
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
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

package cart.domain;

import cart.exception.InvalidMoneyException;
import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    public static final Money ZERO = new Money(0);

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

    public Money multiply(int multiplicand) {
        BigDecimal result = value.multiply(BigDecimal.valueOf(multiplicand));
        return new Money(result);
    }

    public Money multiply(BigDecimal multiplicand) {
        BigDecimal result = value.multiply(multiplicand);
        return new Money(result);
    }

    public Money add(Money otherMoney) {
        BigDecimal result = value.add(otherMoney.value);
        return new Money(result);
    }

    public Money subtract(BigDecimal subtrahend) {
        BigDecimal result = value.subtract(subtrahend);
        return new Money(result);
    }

    public boolean isNotSameValue(BigDecimal otherValue) {
        return this.value.compareTo(otherValue) != 0;
    }

    public boolean isLessThan(BigDecimal otherValue) {
        return this.value.compareTo(otherValue) == -1;
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
        return value.compareTo(money.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

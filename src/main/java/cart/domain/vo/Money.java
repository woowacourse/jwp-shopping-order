package cart.domain.vo;

import cart.exception.MoneyException;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    private final BigDecimal value;

    private Money(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new MoneyException.NotNegative();
        }
    }

    public static Money from(String value) {
        return new Money(new BigDecimal(value));
    }

    public static Money from(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money from(int value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money from(BigDecimal value) {
        return new Money(value);
    }

    public Money plus(Money other) {
        return new Money(value.add(other.value));
    }

    public Money minus(Money other) {
        return new Money(value.subtract(other.value));
    }

    public Money times(Quantity quantity) {
        BigDecimal multiplyMoney = value.multiply(BigDecimal.valueOf(quantity.getValue()));
        return new Money(multiplyMoney);
    }

    public Money times(BigDecimal other) {
        BigDecimal multiplyMoney = value.multiply(other);
        return new Money(multiplyMoney);
    }

    public boolean isEqualOrGreaterThan(Money other) {
        return value.compareTo(other.value) >= 0;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(value, money.value);
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

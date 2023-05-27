package cart.domain.common;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    public static final Money ZERO = Money.from(0);

    private final BigDecimal value;

    Money(BigDecimal value) {
        this.value = value;
    }

    public static Money from(long value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public Money plus(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money minus(Money other) {
        return new Money(this.value.subtract(other.value));
    }

    public Money times(double percent) {
        return new Money(this.value.multiply(BigDecimal.valueOf(percent)));
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return value.compareTo(other.value) >= 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Money)) {
            return false;
        }

        Money object = (Money) o;
        return Objects.equals(value.doubleValue(), object.value.doubleValue());
    }

    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Money{" +
                "value=" + value +
                '}';
    }

    public BigDecimal getValue() {
        return value;
    }

    public long getLongValue() {
        return value.longValue();
    }
}

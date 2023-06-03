package cart.domain;

import cart.exception.IllegalMoneyException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {

    private static final BigDecimal MIN_VALUE = BigDecimal.ZERO;
    public static final Money MIN = Money.from(MIN_VALUE);
    private final BigDecimal value;

    public Money(BigDecimal value) {
        this.value = value;
    }

    public static Money from(BigDecimal value) {
        validate(value);
        return new Money(value);
    }

    public static Money from(int value) {
        return Money.from(BigDecimal.valueOf(value));
    }

    private static void validate(BigDecimal value) {
        if (value.compareTo(MIN_VALUE) < 0) {
            throw new IllegalMoneyException("금액은 음수일 수 없습니다.");
        }
    }

    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        if (this.isLessThan(other)) {
            throw new IllegalMoneyException("원액보다 큰 금액을 차감할 수 없습니다.");
        }
        return Money.from(this.value.subtract(other.value));
    }

    private boolean isLessThan(Money other) {
        return this.value.compareTo(other.value) < 0;
    }

    public Money multiply(int value) {
        return new Money(this.value.multiply(BigDecimal.valueOf(value)));
    }

    public Money multiplyRateAndRound(double rate) {
        return new Money(multiplyRate(rate).setScale(0, RoundingMode.HALF_UP));
    }

    private BigDecimal multiplyRate(double rate) {
        return this.value.multiply(BigDecimal.valueOf(rate));
    }

    public boolean isGreaterThan(Money other) {
        return this.value.compareTo(other.value) > 0;
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

    public int getIntValue() {
        return value.intValue();
    }

    public BigDecimal getValue() {
        return value;
    }
}

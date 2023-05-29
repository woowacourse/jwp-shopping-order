package coupon.domain;

import java.util.Objects;

public final class Money {

    public static final Money ZERO = new Money(0);
    private static final int MIN_VALUE = 0;

    private final long value;

    public Money(long value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(long value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("금액은 " + MIN_VALUE + "원 이상이어야 합니다.");
        }
    }

    public long getValue() {
        return value;
    }

    public Money getMoneyByPercentage(int percentage) {
        return new Money(value * percentage / 100);
    }

    public Money subtractAmountByPercentage(int percentage) {
        return new Money(value - (value * percentage / 100));
    }

    public Money add(Money money) {
        return new Money(value + money.value);
    }

    public boolean isBiggerThan(Money money) {
        return value > money.value;
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
}

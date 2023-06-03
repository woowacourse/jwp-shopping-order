package com.woowahan.techcourse.coupon.domain;

import com.woowahan.techcourse.coupon.exception.CouponException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {

    public static final Money ZERO = new Money(0);
    private static final int MIN_VALUE = 0;

    private final BigDecimal value;

    public Money(long value) {
        this(BigDecimal.valueOf(value));
    }

    public Money(BigDecimal value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(BigDecimal value) {
        if (value.compareTo(new BigDecimal(MIN_VALUE)) < 0) {
            throw new CouponException("금액은 " + MIN_VALUE + "원 이상이어야 합니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    public Money getMoneyByPercentage(int percentage) {
        BigDecimal multiply = value.multiply(new BigDecimal(percentage).divide(new BigDecimal(100)));
        return new Money(multiply);
    }

    public Money subtractAmountByPercentage(int percentage) {
        BigDecimal dividedResult = value.divide(new BigDecimal(percentage), RoundingMode.CEILING);
        BigDecimal resultAmount = value.subtract(dividedResult);
        return new Money(resultAmount);
    }

    public Money add(Money money) {
        return new Money(value.add(money.value));
    }

    public Money subtract(Money discountAmount) {
        return new Money(value.subtract(discountAmount.value));
    }

    public boolean isBiggerThan(Money money) {
        return value.compareTo(money.value) > 0;
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

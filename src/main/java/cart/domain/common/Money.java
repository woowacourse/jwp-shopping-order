package cart.domain.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {

    private static final int MINIMUM_PRICE = 0;
    private static final int DEFAULT_SCALE = 0;

    private final BigDecimal money;

    public Money(final BigDecimal money) {
        this.money = money;
    }

    public static Money valueOf(final int money) {
        validate(money);
        return new Money(new BigDecimal(Integer.toString(money)));
    }

    private static void validate(final int price) {
        if (price < MINIMUM_PRICE) {
            throw new IllegalArgumentException("돈은 " + MINIMUM_PRICE + " 미만일 수 없습니다.");
        }
    }

    public Money add(final Money money) {
        return new Money(this.money.add(money.money));
    }

    public Money subtract(final Money money) {
        return new Money(this.money.subtract(money.money));
    }

    public boolean isMoreThan(final Money money) {
        return this.money.compareTo(money.money) > 0;
    }

    public Money getPartial(final double rate) {
        return new Money(money.multiply(BigDecimal.valueOf(rate)).setScale(DEFAULT_SCALE, RoundingMode.DOWN));
    }

    public BigDecimal getMoney() {
        return money;
    }

    public int getMoneyValue() {
        return money.intValue();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Money money1 = (Money) o;
        return Objects.equals(money, money1.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }

    @Override
    public String toString() {
        return "Money{" +
                "money=" + money +
                '}';
    }
}

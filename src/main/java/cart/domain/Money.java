package cart.domain;

import java.math.BigInteger;
import java.util.Objects;

public class Money {

    private static final int MINIMUM_PRICE = 0;

    private final BigInteger money;

    public Money(final int money) {
        validate(money);
        this.money = new BigInteger(Integer.toString(money));
    }

    private void validate(final int price) {
        if (price < MINIMUM_PRICE) {
            throw new IllegalArgumentException("돈은 " + MINIMUM_PRICE + " 미만일 수 없습니다.");
        }
    }

    public BigInteger getMoney() {
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
}

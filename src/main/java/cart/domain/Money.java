package cart.domain;

import cart.exception.MoneyException;

public class Money {
    private final long value;

    public Money(long value) {
        validateMoney(value);
        this.value = value;
    }

    private void validateMoney(long money) {
        if (money < 0) {
            throw new MoneyException.InvalidRange(money);
        }
    }

    public Money minusMoney(long money) {
        return new Money(this.value - money);
    }

    public long getValue() {
        return value;
    }
}

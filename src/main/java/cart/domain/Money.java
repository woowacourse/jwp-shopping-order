package cart.domain;

import cart.exception.MoneyException;

public class Money {

    private final long value;

    public Money(final long value) {
        validate(value);
        this.value = value;
    }

    private void validate(final long value) {
        if (value < 0) {
            throw new MoneyException.IllegalValue(value);
        }
    }

    public long getValue() {
        return value;
    }
}

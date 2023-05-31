package cart.domain;

import cart.exception.InvalidMoneyException;
import java.math.BigDecimal;

public class Money {

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

    public BigDecimal getValue() {
        return value;
    }
}

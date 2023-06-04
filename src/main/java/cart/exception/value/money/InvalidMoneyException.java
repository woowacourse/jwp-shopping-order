package cart.exception.value.money;

import cart.exception.value.ValueException;

public class InvalidMoneyException extends ValueException {

    private static final String INVALID_MONEY_EXCEPTION_MESSAGE = "금액은 음수가 될 수 없습니다.";

    public InvalidMoneyException() {
        super(INVALID_MONEY_EXCEPTION_MESSAGE);
    }
}

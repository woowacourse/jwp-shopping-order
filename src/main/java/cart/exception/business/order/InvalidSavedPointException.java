package cart.exception.business.order;

import cart.exception.business.BusinessException;

public class InvalidSavedPointException extends BusinessException {

    private static final String MESSAGE = "적립 포인트는 음수가 될 수 없습니다. 입력한 적립 포인트: %d";

    public InvalidSavedPointException(final int savedPoint) {
        super(String.format(MESSAGE, savedPoint));
    }
}

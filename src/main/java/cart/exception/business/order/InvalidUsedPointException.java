package cart.exception.business.order;

import cart.exception.business.BusinessException;

public class InvalidUsedPointException extends BusinessException {

    private static final String MESSAGE = "사용한 포인트는 음수가 될 수 없습니다. 입력한 사용 포인트: %d";

    public InvalidUsedPointException(final int usedPoint) {
        super(String.format(MESSAGE, usedPoint));
    }
}

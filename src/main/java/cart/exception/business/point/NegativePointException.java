package cart.exception.business.point;

import cart.exception.business.BusinessException;

public class NegativePointException extends BusinessException {

    private static final String MESSAGE = "포인트는 음수값일 수 없습니다.";

    public NegativePointException() {
        super(MESSAGE);
    }
}

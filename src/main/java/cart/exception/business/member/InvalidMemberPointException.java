package cart.exception.business.member;

import cart.exception.business.BusinessException;

public class InvalidMemberPointException extends BusinessException {

    private static final String MESSAGE = "포인트는 음수값일 수 없습니다.";

    public InvalidMemberPointException() {
        super(MESSAGE);
    }
}

package cart.exception.business.member;

import cart.exception.business.BusinessException;

public class InvalidMemberEmailException extends BusinessException {

    private static final String MESSAGE = "사용자의 이메일 형식이 잘못되었습니다. 입력한 이메일: %s";

    public InvalidMemberEmailException(final String email) {
        super(String.format(MESSAGE, email));
    }
}

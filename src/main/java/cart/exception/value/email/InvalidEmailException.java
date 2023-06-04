package cart.exception.value.email;

import cart.exception.value.ValueException;

public class InvalidEmailException extends ValueException {

    private static final String INVALID_EMAIL_EXCEPTION_MESSAGE = "올바르지 않은 이메일 형식입니다.";

    public InvalidEmailException() {
        super(INVALID_EMAIL_EXCEPTION_MESSAGE);
    }
}

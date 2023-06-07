package cart.exception.value.password;

import cart.exception.value.ValueException;

public class InvalidPasswordException extends ValueException {

    private static final String INVALID_PASSWORD_EXCEPTION_MESSAGE = "비밀번호는 8~16자의 영문 대 소문자, 숫자, 특수문자를 사용해야합니다.";

    public InvalidPasswordException() {
        super(INVALID_PASSWORD_EXCEPTION_MESSAGE);
    }
}

package cart.exception.authentication;

public class InvalidFormatException extends AuthenticationException {

    private static final String MESSAGE = "이메일과 비밀번호를 모두 입력해야 합니다.";

    public InvalidFormatException() {
        super(MESSAGE);
    }
}

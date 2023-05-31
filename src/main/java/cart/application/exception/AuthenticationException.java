package cart.application.exception;

public class AuthenticationException extends RuntimeException {

    private static final String MESSAGE = "인증 오류가 발생했습니다.";

    public AuthenticationException() {
        super(MESSAGE);
    }
}

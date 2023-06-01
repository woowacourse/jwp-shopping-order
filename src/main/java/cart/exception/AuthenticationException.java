package cart.exception;

public class AuthenticationException extends RuntimeException {

    private static final String MESSAGE = "존재하지 않는 사용자입니다.";

    public AuthenticationException() {
        super(MESSAGE);
    }
}

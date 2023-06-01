package cart.exception;

public class AuthenticationException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "인증 정보가 잘못 되었습니다.";

    public AuthenticationException() {
        super(DEFAULT_MESSAGE);
    }

    public AuthenticationException(final String message) {
        super(message);
    }
}

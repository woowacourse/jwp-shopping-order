package cart.exception.auth;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("인증에 실패했습니다.");
    }

    public AuthenticationException(final String message) {
        super(message);
    }
}

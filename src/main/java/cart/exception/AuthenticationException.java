package cart.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public static class InvalidScheme extends AuthenticationException {
        public InvalidScheme() {
            super("올바른 인증 방식이 아닙니다.");
        }
    }
}

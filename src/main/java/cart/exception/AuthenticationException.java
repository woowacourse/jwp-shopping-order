package cart.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public static class Unauthorized extends AuthenticationException {
        public Unauthorized(String message) {
            super(message);
        }
    }

    public static class LoginFail extends AuthenticationException {
        public LoginFail(String message) {
            super(message);
        }
    }
}

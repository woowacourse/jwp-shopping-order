package cart.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    public static class Unauthorized extends AuthenticationException {
        public Unauthorized(String message) {
            super(message);
        }
    }

    public static class LoginFail extends AuthenticationException {
        public LoginFail() {
            super("로그인 정보가 잘못되었습니다.");
        }
    }
}

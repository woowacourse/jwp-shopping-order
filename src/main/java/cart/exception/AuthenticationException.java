package cart.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super();
    }

    public AuthenticationException(final String message) {
        super(message);
    }

    public static class NotFound extends AuthenticationException {
        public NotFound() {
            super("해당 유저를 찾을 수 없습니다.");
        }
    }

    public static class InvalidScheme extends AuthenticationException {
        public InvalidScheme() {
            super("올바른 인증 방식이 아닙니다.");
        }
    }

    public static class InvalidCredentials extends AuthenticationException {
        public InvalidCredentials() {
            super("유저 정보가 올바르지 않습니다.");
        }
    }
}

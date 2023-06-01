package cart.exception;

public abstract class AuthenticationException extends RuntimeException {

    private AuthenticationException(String message) {
        super(message);
    }

    private AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class InvalidTokenFormat extends AuthenticationException {

        public InvalidTokenFormat(String message) {
            super(message);
        }

        public InvalidTokenFormat(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class ForbiddenMember extends AuthenticationException {

        public ForbiddenMember() {
            super("로그인이 필요한 기능입니다.");
        }
    }

    public static class InvalidMember extends AuthenticationException {

        public InvalidMember() {
            super("유효하지 않은 회원입니다.");
        }
    }
}

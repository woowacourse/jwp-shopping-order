package cart.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public static class InvalidMember extends AuthenticationException {
        public InvalidMember() {
            super("존재하지 않는 사용자입니다.");
        }
    }
}

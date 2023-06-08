package cart.exception;

public class MemberException extends RuntimeException {
    public MemberException(String message) {
        super(message);
    }

    public static class NotFound extends MemberException {
        public NotFound(String email) {
            super("해당 이메일의 멤버를 찾을 수 없습니다. email: " + email);
        }
    }
}

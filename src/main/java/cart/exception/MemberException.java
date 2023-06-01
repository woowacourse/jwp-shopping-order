package cart.exception;

public abstract class MemberException extends RuntimeException {

    private MemberException(String message) {
        super(message);
    }

    public static class NotFound extends MemberException {

        public NotFound(Long id) {
            super("해당 사용자를 찾을 수 없습니다 : " + id);
        }

        public NotFound(String email) {
            super("해당 사용자를 찾을 수 없습니다 : " + email);
        }
    }

    
}

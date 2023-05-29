package cart.exception;

public class MemberException extends ShoppingException {

    public MemberException(String message) {
        super(message);
    }

    public static class NotFound extends MemberException {

        public NotFound() {
            super("해당 멤버가 존재하지 않습니다.");
        }
    }
}

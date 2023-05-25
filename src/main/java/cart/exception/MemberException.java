package cart.exception;

public class MemberException extends RuntimeException {

    public MemberException(String message) {
        super(message);
    }

    public static class InvalidPassword extends MemberException {

        public InvalidPassword(String message) {
            super(message);
        }
    }

    public static class InvalidEmail extends MemberException {
        public InvalidEmail(String message) {
            super(message);
        }
    }
}

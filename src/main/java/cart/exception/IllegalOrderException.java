package cart.exception;

public class IllegalOrderException extends RuntimeException {

    public IllegalOrderException(String message) {
        super(message);
    }

    public static class IllegalMember extends IllegalOrderException {
        public IllegalMember() {
            super("올바르지 않은 요청입니다");
        }
    }
}

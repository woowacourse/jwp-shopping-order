package cart.exception;

public class InvalidRequestException extends RuntimeException{

    public InvalidRequestException(final String message) {
        super(message);
    }

    public static class ExceedHavingInvalidRequest extends InvalidRequestException {
        public ExceedHavingInvalidRequest() {
            super("보유한 포인트보다 많은 포인트는 사용할 수 없습니다.");
        }
    }

    public static class ExceedPrice extends InvalidRequestException {
        public ExceedPrice() {
            super("주문금액보다 높은 포인트는 사용할 수 없습니다.");
        }
    }

    public static class MinusInvalidRequest extends InvalidRequestException {
        public MinusInvalidRequest() {
            super("포인트는 0보다 작을 수 없습니다.");
        }
    }
}

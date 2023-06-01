package cart.exception;

public class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }

    public static class NotFound extends OrderException {

        public NotFound() {
            super("해당 주문정보를 찾을 수 없습니다.");
        }
    }
}

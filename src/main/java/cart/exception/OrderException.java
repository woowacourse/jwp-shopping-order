package cart.exception;

public class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }

    public static class NotFound extends OrderException {

        public NotFound() {
            super("해당하는 주문이 존재하지 않습니다.");
        }
    }
}

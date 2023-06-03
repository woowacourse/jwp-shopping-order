package cart.exception;

public class OrderException extends RuntimeException {

    public OrderException(final String message) {
        super(message);
    }

    public static class NotFound extends OrderException {
        public NotFound(final Long id) {
            super(id + " ID를 가진 주문을 찾을 수 없습니다.");
        }
    }
}

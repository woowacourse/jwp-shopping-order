package cart.exception;

public class OrderException extends RuntimeException {

    public OrderException(final String message) {
        super(message);
    }

    public static class OrderNotExistException extends OrderException {

        public OrderNotExistException(final String message) {
            super(message);
        }
    }
}

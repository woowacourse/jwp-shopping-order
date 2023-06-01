package cart.exception;

public class OrderAuthorizationException extends RuntimeException {
    public OrderAuthorizationException(final String message) {
        super(message);
    }
}

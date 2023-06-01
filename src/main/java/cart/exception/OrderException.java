package cart.exception;

public class OrderException extends RuntimeException {

    private final String message;

    public OrderException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

package cart.exception.common;

public class CartException extends RuntimeException {

    private final String message;

    public CartException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

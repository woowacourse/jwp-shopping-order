package cart.exception;

public class StoreException extends RuntimeException {

    private final String message;

    public StoreException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

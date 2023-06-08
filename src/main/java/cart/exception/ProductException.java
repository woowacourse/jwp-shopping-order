package cart.exception;

public class ProductException extends RuntimeException {

    private final String message;

    public ProductException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

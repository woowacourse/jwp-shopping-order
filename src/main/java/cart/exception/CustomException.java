package cart.exception;

public class CustomException extends RuntimeException {
    private final String message;

    public CustomException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

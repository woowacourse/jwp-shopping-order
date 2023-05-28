package cart.exception;

public class GlobalException extends RuntimeException {

    private final String message;

    public GlobalException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

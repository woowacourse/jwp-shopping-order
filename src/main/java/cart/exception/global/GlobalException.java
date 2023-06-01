package cart.exception.global;

public abstract class GlobalException extends RuntimeException {

    private final String message;

    public GlobalException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

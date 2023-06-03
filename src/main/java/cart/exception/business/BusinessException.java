package cart.exception.business;

public abstract class BusinessException extends RuntimeException {

    private final String message;

    public BusinessException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

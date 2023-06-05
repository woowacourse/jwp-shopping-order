package cart.exception;

public class InvalidRequestValueException extends RuntimeException {
    private final int errorCode;
    private final String message;

    public InvalidRequestValueException(final int errorCode, final String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

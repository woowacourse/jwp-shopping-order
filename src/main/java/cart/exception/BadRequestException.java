package cart.exception;

public class BadRequestException extends RuntimeException {

    private final ErrorCode errorCode;

    public BadRequestException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

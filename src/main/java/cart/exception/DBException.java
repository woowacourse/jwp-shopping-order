package cart.exception;

public class DBException extends RuntimeException {

    private final ErrorCode errorCode;

    public DBException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

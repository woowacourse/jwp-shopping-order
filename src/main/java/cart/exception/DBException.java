package cart.exception;

public class DBException extends RuntimeException {

    public DBException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}

package cart.exception;

import org.springframework.http.HttpStatus;

public class DBException extends RuntimeException {

    private static final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    private final ErrorCode errorCode;

    public DBException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

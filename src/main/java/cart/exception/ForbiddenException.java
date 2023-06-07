package cart.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends RuntimeException {

    private static final HttpStatus httpStatus = HttpStatus.FORBIDDEN;
    private final ErrorCode errorCode;

    public ForbiddenException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

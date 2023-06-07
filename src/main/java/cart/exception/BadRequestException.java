package cart.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {

    private static final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    private final ErrorCode errorCode;

    public BadRequestException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

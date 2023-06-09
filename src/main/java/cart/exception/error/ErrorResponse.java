package cart.exception.error;

import cart.exception.BaseException;

public class ErrorResponse {

    private final int errorCode;
    private final String message;

    public ErrorResponse(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ErrorResponse create(final BaseException exception) {
        return new ErrorResponse(exception.getErrorCode(), exception.getErrorMessage());
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}

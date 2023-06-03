package cart.exception;

public class ErrorResponse {

    private final ErrorCode errorCode;
    private final String errorMessage;

    public ErrorResponse(final ErrorCode errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

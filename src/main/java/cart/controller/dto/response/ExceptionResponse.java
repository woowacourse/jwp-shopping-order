package cart.controller.dto.response;

public class ExceptionResponse {

    private final int errorCode;
    private final String message;

    public ExceptionResponse(final int errorCode, final String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }
}

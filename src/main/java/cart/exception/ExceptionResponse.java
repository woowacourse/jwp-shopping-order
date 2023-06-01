package cart.exception;

public class ExceptionResponse {

    private final String message;
    private final Integer errorCode;

    public ExceptionResponse(String message, Integer errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}

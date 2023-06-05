package cart.dto.response;

public class ExceptionResponse {
    private final Integer errorCode;
    private final String errorMessage;

    public ExceptionResponse(final Integer errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

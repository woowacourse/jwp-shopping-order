package cart.application.dto;

public class ErrorResponse {

    private final String errorType;

    public ErrorResponse(final String errorType) {
        this.errorType = errorType;
    }

    public String getErrorType() {
        return errorType;
    }
}

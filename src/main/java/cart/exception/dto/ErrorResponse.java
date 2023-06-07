package cart.exception.dto;

public class ErrorResponse {
    private final String message;
    private final String exception;

    public ErrorResponse(final String message, final String exception) {
        this.message = message;
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public String getException() {
        return exception;
    }
}

package cart.dto.response.exception;

public class ExceptionResponse {

    private String message;

    public ExceptionResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

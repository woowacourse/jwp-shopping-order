package cart.dto.response;

public class ExceptionResponse {

    private String message;

    public ExceptionResponse() {
    }

    public ExceptionResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

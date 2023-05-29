package cart.dto;

public class ExceptionResponse {

    private String message;

    public ExceptionResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

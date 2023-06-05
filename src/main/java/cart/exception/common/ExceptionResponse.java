package cart.exception.common;

public class ExceptionResponse {

    public final String message;

    public ExceptionResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

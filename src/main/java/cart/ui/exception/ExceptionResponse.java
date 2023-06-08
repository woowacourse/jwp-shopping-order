package cart.ui.exception;

public class ExceptionResponse {
    private final String message;

    public ExceptionResponse(final String message) {
        this.message = message;
    }

    public String getName() {
        return message;
    }
}

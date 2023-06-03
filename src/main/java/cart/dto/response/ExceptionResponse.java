package cart.dto.response;

public class ExceptionResponse {
    private final String message;

    private ExceptionResponse(final String message) {
        this.message = message;
    }

    public static ExceptionResponse of(final String message){
        return new ExceptionResponse(message);
    }
}

package cart.dto;

public class ExceptionResponse extends RuntimeException {

    public ExceptionResponse(String message) {
        super(message);
    }
}

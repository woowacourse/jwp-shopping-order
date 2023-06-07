package cart.exception;

public class ExpectedException extends RuntimeException {

    public ExpectedException(String message) {
        super(message);
    }

    public ExpectedException(String message, Throwable cause) {
        super(message, cause);
    }
}

package cart.exception;

public class NotOwnerException extends IllegalArgumentException {

    public NotOwnerException(final String message) {
        super(message);
    }
}

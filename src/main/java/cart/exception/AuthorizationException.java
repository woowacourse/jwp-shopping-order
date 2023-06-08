package cart.exception;

public class AuthorizationException extends IllegalArgumentException {

    public AuthorizationException(final String message) {
        super(message);
    }
}

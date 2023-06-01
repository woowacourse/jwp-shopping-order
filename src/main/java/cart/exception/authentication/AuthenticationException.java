package cart.exception.authentication;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
    }

    public AuthenticationException(final String message) {
        super(message);
    }
}

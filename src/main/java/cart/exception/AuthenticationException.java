package cart.exception;

public class AuthenticationException extends CustomException {

    public AuthenticationException(final ErrorMessage message) {
        super(message);
    }
}

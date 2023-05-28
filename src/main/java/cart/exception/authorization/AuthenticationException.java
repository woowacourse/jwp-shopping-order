package cart.exception.authorization;

public class AuthenticationException extends RuntimeException {

    private final String message;

    public AuthenticationException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

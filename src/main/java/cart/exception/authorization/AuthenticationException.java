package cart.exception.authorization;

public abstract class AuthenticationException extends RuntimeException {

    private final String message;

    public AuthenticationException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

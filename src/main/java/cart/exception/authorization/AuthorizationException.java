package cart.exception.authorization;

abstract public class AuthorizationException extends RuntimeException {

    private final String message;

    public AuthorizationException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

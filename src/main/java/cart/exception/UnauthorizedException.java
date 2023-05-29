package cart.exception;

public class UnauthorizedException extends StoreException {

    public UnauthorizedException(final String message) {
        super(message);
    }
}

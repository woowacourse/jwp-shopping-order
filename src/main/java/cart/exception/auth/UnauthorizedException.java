package cart.exception.auth;

import cart.exception.StoreException;

public class UnauthorizedException extends StoreException {

    public UnauthorizedException(final String message) {
        super(message);
    }
}

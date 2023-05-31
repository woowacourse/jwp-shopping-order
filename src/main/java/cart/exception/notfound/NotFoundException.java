package cart.exception.notfound;

import cart.exception.StoreException;

public class NotFoundException extends StoreException {

    public NotFoundException(final String message) {
        super(message);
    }
}

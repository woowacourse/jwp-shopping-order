package cart.exception.order;

import cart.exception.common.CartException;

public class InvalidOrderException extends CartException {

    public InvalidOrderException(final String message) {
        super(message);
    }
}

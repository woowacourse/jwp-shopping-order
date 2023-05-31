package cart.exception.cart;

import cart.exception.common.CartException;

public class InvalidOrderException extends CartException {

    public InvalidOrderException(final String message) {
        super(message);
    }
}

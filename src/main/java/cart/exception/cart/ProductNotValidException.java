package cart.exception.cart;

import cart.exception.common.CartException;

public class ProductNotValidException extends CartException {

    public ProductNotValidException(final String message) {
        super(message);
    }
}

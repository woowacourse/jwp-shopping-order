package cart.domain.cartitem;

import cart.exception.badrequest.cartitem.CartItemQuantityException;

class Quantity {

    private static final int MINIMUM_VALUE = 1;

    private final int value;

    public Quantity(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM_VALUE) {
            throw new CartItemQuantityException(MINIMUM_VALUE, value);
        }
    }

    public int getValue() {
        return value;
    }
}

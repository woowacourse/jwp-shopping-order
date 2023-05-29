package cart.domain.cartitem;

import static cart.exception.CartItemException.QuantityShortage;

class Quantity {

    private static final int MINIMUM_VALUE = 1;

    private final int value;

    public Quantity(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM_VALUE) {
            throw new QuantityShortage(value, MINIMUM_VALUE);
        }
    }

    public int getValue() {
        return value;
    }
}

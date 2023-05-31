package cart.domain.order;

import static cart.exception.OrderException.IllegalMinimumQuantity;

class Quantity {

    private static final int MINIMUM_VALUE = 1;

    private final int value;

    public Quantity(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM_VALUE) {
            throw new IllegalMinimumQuantity(value, MINIMUM_VALUE);
        }
    }

    public int getValue() {
        return value;
    }
}

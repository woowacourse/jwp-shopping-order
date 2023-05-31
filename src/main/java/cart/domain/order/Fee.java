package cart.domain.order;

import cart.exception.OrderException.IllegalFee;

class Fee {

    private static final int MINIMUM_VALUE = 0;

    private final int value;

    public Fee(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM_VALUE) {
            throw new IllegalFee();
        }
    }

    public int getValue() {
        return value;
    }
}

package cart.domain.product;

import cart.exception.ProductException.NegativePrice;

class Price {

    private static final int MINIMUM_VALUE = 0;

    private final int value;

    public Price(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM_VALUE) {
            throw new NegativePrice();
        }
    }

    public int getValue() {
        return value;
    }
}

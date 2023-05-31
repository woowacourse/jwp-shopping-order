package cart.domain.order;

import static cart.exception.OrderException.IllegalPoint;

class Point {

    private static final int MINIMUM_VALUE = 0;

    private final int value;

    public Point(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM_VALUE) {
            throw new IllegalPoint();
        }
    }

    public int getValue() {
        return value;
    }
}

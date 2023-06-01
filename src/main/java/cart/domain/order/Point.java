package cart.domain.order;

import static cart.exception.OrderException.IllegalPoint;

class Point {

    private static final int MINIMUM_VALUE = 0;
    private static final double REWORD_RATE = 0.1;

    private final int value;

    public Point(int value) {
        validate(value);
        this.value = value;
    }

    public static Point from(int totalPrice) {
        if (totalPrice < MINIMUM_VALUE) {
            throw new IllegalPoint();
        }
        return new Point((int) Math.ceil(totalPrice * REWORD_RATE));
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

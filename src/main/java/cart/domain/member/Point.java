package cart.domain.member;

import static cart.exception.MemberException.NegativePoint;

class Point {

    private static final int MINIMUM_VALUE = 0;

    private final int value;

    public Point(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM_VALUE) {
            throw new NegativePoint();
        }
    }

    public Point decrease(int point) {
        if (point < MINIMUM_VALUE) {
            throw new NegativePoint();
        }
        return new Point(value - point);
    }

    public int getValue() {
        return value;
    }
}

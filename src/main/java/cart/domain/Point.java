package cart.domain;

import cart.exception.CartItemException.PointNotPositive;

public class Point {

    private final int value;

    private Point(final int value) {
        this.value = value;
    }

    public static Point of(final int point) {
        validatePoint(point);
        return new Point(point);
    }

    private static void validatePoint(final int point) {
        if (point < 0) {
            throw new PointNotPositive();
        }
    }

    public int getValue() {
        return value;
    }
}

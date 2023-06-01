package cart.domain;

import cart.exception.ErrorStatus;
import cart.exception.ShoppingOrderException;

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
            throw new ShoppingOrderException(ErrorStatus.POINT_INVALID_INPUT);
        }
    }

    public int getValue() {
        return value;
    }
}

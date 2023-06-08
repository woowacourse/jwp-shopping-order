package cart.domain;

import cart.exception.ErrorStatus;
import cart.exception.ShoppingOrderException;
import java.util.Objects;

public class Point {

    private static final double EARNING_RATE = 0.05;
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
            throw new ShoppingOrderException(ErrorStatus.POINT_NOT_NEGATIVE);
        }
    }

    public Point use(Integer point) {
        return use(of(point));
    }

    public Point use(Point point) {
        return new Point(value - point.value);
    }

    public Point collect(Integer originalPrice) {
        int plusPoint = (int) Math.ceil((double) originalPrice * EARNING_RATE);
        return collect(of(plusPoint));
    }

    public Point collect(Point point) {
        return new Point(value + point.value);
    }

    public boolean isLessThan(Integer point) {
        return value < point;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return value == point.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

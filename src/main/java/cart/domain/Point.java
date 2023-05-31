package cart.domain;

import java.util.Objects;

import cart.exception.NegativePointException;

public class Point {

    private static final int MINIMUM_POINT = 0;

    private final int value;

    private Point(final int value) {
        validate(value);
        this.value = value;
    }

    public static Point valueOf(final int value) {
        return new Point(value);
    }

    private void validate(final int value) {
        if (value < MINIMUM_POINT) {
            throw new NegativePointException();
        }
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
        final Point point = (Point) o;
        return value == point.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

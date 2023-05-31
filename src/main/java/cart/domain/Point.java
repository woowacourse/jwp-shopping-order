package cart.domain;

import cart.exception.InvalidPointException;
import java.util.Objects;

public class Point {

    private static final double ACCUMULATION_RATE = 0.1;

    private final int value;

    public Point(int value) {
        validate(value);
        this.value = value;
    }

    public static Point fromPayment(final int payment) {
        return new Point((int) Math.round(payment * ACCUMULATION_RATE));
    }

    private void validate(final int value) {
        if (value < 0) {
            throw new InvalidPointException("포인트는 음수가 될 수 없습니다.");
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

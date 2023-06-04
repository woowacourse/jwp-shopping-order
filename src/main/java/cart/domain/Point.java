package cart.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

import cart.exception.NegativePointException;

public class Point {

    public static final Point ZERO = Point.valueOf(0);
    private static final int MINIMUM_POINT = 0;
    private static final int EXPIRED_MONTH = 6;
    private static final double EARNING_RATE = 0.05;

    private final int value;

    private Point(final int value) {
        validate(value);
        this.value = value;
    }

    public static Point valueOf(final int value) {
        return new Point(value);
    }

    public static Timestamp getExpiredAt(final Timestamp createdAt) {
        final LocalDateTime localDateTime = createdAt.toLocalDateTime();
        final LocalDateTime expiredAt = localDateTime.plusMonths(EXPIRED_MONTH);
        return Timestamp.valueOf(expiredAt);
    }

    public static double getEarningRate() {
        return EARNING_RATE * 100;
    }

    public static Point getEarnedPoint(final Price price) {
        final int earnedPoint = (int) (EARNING_RATE * price.getValue());
        return new Point(earnedPoint);
    }

    private void validate(final int value) {
        if (value < MINIMUM_POINT) {
            throw new NegativePointException();
        }
    }

    public boolean isLessThan(final Point other) {
        return value < other.value;
    }

    public Point subtract(final Point other) {
        return new Point(value - other.value);
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

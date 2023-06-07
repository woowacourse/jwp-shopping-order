package cart.domain;

import cart.exception.OrderException;

import java.util.Objects;

public class Point {

    public static final int DEFAULT_VALUE = 0;
    public static final int MIN_USAGE_VALUE = 3_000;
    private static final double POINT_RATE = 0.05;

    private final int value;

    public Point(final int value) {
        validatePositive(value);
        this.value = value;
    }

    private void validatePositive(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("포인트는 음수가 될 수 없습니다.");
        }
    }

    public void validateUsablePoint(final int usePoint) {
        if (0 < usePoint && usePoint < MIN_USAGE_VALUE) {
            throw new OrderException.IllegalUsePoint("최소 사용 기준 포인트보다 작은 값의 포인트는 사용할 수 없습니다.");
        }
    }

    public Point calculatePointBy(final int totalProductPrice) {
        int value = (int) Math.ceil(totalProductPrice * POINT_RATE);

        return new Point(value);
    }

    public Point add(final Point other) {
        return new Point(this.value + other.value);
    }

    public void validateUsePoint(final Point usePoint) {
        if (value < usePoint.value) {
            throw new OrderException.IllegalUsePoint(value, usePoint.value);
        }
    }

    public Point subtract(final Point other) {
        return new Point(this.value - other.value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return value == point.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

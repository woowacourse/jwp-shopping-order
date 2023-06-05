package cart.domain;

import cart.exception.PointException;

public class Point {
    private final long value;

    public Point(long value) {
        validatePoint(value);
        this.value = value;
    }

    private void validatePoint(long value) {
        if (value < 0) {
            throw new PointException.InvalidRange(value);
        }
    }

    public Point addPoint(long point) {
        return new Point(this.value + point);
    }

    public Point minusPoint(long point) {
        return new Point(this.value - point);
    }

    public long getValue() {
        return value;
    }

    public Point plusPointByPolicy(long useMoney) {
        return new Point(this.value + pointPolicy(useMoney));
    }

    private long pointPolicy(long useMoney) {
        return (useMoney / 100) * 10;
    }
}

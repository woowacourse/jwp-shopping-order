package cart.domain;

import java.math.BigDecimal;

public class Point {
    private final BigDecimal point;

    public Point(final BigDecimal point) {
        validateMinimum(point);
        this.point = point;
    }

    private void validateMinimum(BigDecimal point) {
        if (point.longValue() < BigDecimal.ZERO.longValue()) {
            throw new IllegalArgumentException("포인트는 0 이상이어야 합니다");
        }
    }

    public Point addPoint(Point point) {
        return new Point(this.point.add(point.point));
    }

    public Point subtractPoint(Point point) {
        final BigDecimal newPoint = this.point.subtract(point.point);
        validateMinimum(point.point);
        return new Point(newPoint);
    }

    public BigDecimal getPoint() {
        return point;
    }
}

package cart.domain.point;

import cart.exception.point.PointException;

import java.util.Objects;

public class Point {

    private static final double POINT_POLICY = 0.1;
    private final Long point;

    public Point(Long point) {
        this.point = point;
    }

    public Long minus(final Long requestPoint) {
        if (this.point < requestPoint) {
            throw new PointException.OverThenMemberPoint();
        }
        return this.point - requestPoint;
    }

    public Long getPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point1 = (Point) o;
        return Objects.equals(point, point1.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point);
    }
}

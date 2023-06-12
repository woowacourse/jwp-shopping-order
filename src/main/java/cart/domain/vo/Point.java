package cart.domain.vo;

import cart.domain.payment.PointPolicy;
import cart.exception.PointException;

import java.util.Objects;

public class Point {

    private static final int INITIAL_POINT = 1_000;
    public static final int MINIMUM_POINT = 0;

    private final int point;

    public Point() {
        this.point = INITIAL_POINT;
    }

    public Point(int point) {
        validateAmount(point);
        this.point = point;
    }

    private void validateAmount(int point) {
        if (point < MINIMUM_POINT) {
            throw new PointException.InvalidUsedPoint();
        }
    }

    public Point consume(Point usedPoint) {
        validatePointToUse(usedPoint);
        return new Point(point - usedPoint.point);
    }

    private void validatePointToUse(Point usedPoint) {
        if (usedPoint.isGreaterThan(this)) {
            throw new PointException.InvalidUsedPoint();
        }
    }

    public boolean isGreaterThan(Point pointToUse) {
        return this.point > pointToUse.point;
    }

    public Point earnPoint(PointPolicy pointPolicy, Cash userPayment) {
        int newPoint = pointPolicy.calculateEarningPoint(userPayment.getCash());
        return new Point(this.point + newPoint);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point1 = (Point) o;
        return point == point1.point;
    }

    @Override
    public int hashCode() {
        return Objects.hash(point);
    }

    @Override
    public String toString() {
        return "Point{" +
                "point=" + point +
                '}';
    }

    public int getPoint() {
        return point;
    }
}

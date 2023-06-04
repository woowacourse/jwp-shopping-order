package cart.domain;

import java.util.Objects;

public class Point {

    private static final double POINT_POLICY = 0.1;
    private Long point;

    public Point(Long point) {
        this.point = point;
    }

    public Long minus(final Long requestPoint) {
        if (this.point < requestPoint) {
            throw new IllegalStateException("회원의 포인트가 부족합니다.");
        }
        return this.point - requestPoint;
    }

    public Long getPointByPolicy(Long amountPayment) {
        double caculatedPoint = amountPayment * POINT_POLICY;
        return (long) caculatedPoint;
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

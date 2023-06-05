package cart.domain.point;

public class Point {

    private static final Long MIN_POINT = 0L;
    private static final double EARN_POINT_RATE = 2.5 / 100;

    private final Long point;

    public Point(final Long point) {
        validatePositive(point);
        this.point = point;
    }

    private void validatePositive(final Long point) {
        if (MIN_POINT > point) {
            throw new IllegalArgumentException("포인트는 최소 " + MIN_POINT + " 포인트 이상이어야합니다.");
        }
    }

    public Point add(final Point other) {
        return new Point(this.point + other.point);
    }

    public Point subtract(final Point other) {
        return new Point(this.point - other.point);
    }

    public boolean isMoreThan(final Point other) {
        return this.point > other.point;
    }

    public Long getPoint() {
        return point;
    }
}

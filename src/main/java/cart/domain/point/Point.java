package cart.domain.point;

public class Point {

    private static final int MIN_POINT = 0;

    private final int point;

    public Point(final int point) {
        validatePositive(point);
        this.point = point;
    }

    private void validatePositive(final int point) {
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

    public int getPoint() {
        return point;
    }
}

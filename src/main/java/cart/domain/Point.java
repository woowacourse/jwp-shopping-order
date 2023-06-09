package cart.domain;

public class Point {
    private static Point JOIN_EVENT_POINT = new Point(5000);
    private final int point;

    public static Point joinEvent() {
        return JOIN_EVENT_POINT;
    }

    public Point(final int point) {
        this.point = point;
    }

    public Point usePoint(final int usedPoint) {
        return new Point(point - usedPoint);
    }

    public int getPoint() {
        return point;
    }
}

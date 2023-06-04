package cart.domain;

public class Point {
    public static final double POINT_POLICY = 0.01;

    private final int point;

    public Point(int point) {
        this.point = point;
    }

    public static Point calcualtePoint(int totalPrice) {
        return new Point((int) (totalPrice * POINT_POLICY));
    }

    public Point use(Point usedPoint) {
        return new Point(point - usedPoint.point);
    }

    public Point save(Point savedPoint) {
        return new Point(point + savedPoint.point);
    }

    public int getPoint() {
        return point;
    }
}

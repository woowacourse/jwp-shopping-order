package cart.domain;

public class Point {
    public static final Point MINIMUM_USAGE_POINT = new Point(3000);

    private final int value;

    public Point(int value) {
        this.value = value;
    }

    public Point add(Point other) {
        return new Point(this.value + other.value);
    }

    public Point subtract(Point other) {
        return new Point(this.value - other.value);
    }

    public int getValue() {
        return value;
    }
}

package cart.domain;

public class Point {

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

    public boolean isSmallerThan(Point other) {
        return this.value < other.value;
    }

    public boolean isZero() {
        return this.value == 0;
    }

    public int getValue() {
        return value;
    }
}

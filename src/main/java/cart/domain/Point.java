package cart.domain;

import cart.domain.Product.Price;

import java.util.Objects;

public class Point {
    private static final double POINT_RATE = 2.5/100;

    private final int point;

    public Point(int point) {
        this.point = point;
    }

    public static Point from(int point) {
        validatePoint(point);
        return new Point(point);
    }

    public static Point makePointFrom(Price price){
        return price.makePointFrom(POINT_RATE);
    }


    private static void validatePoint(int point) {
        if (point < 0) {
            throw new IllegalArgumentException("포인트는 0보다 작을 수 없습니다.");
        }
    }

    public void validateIsSameOrBiggerThan(Point other){
        if(this.point < other.point){
            throw new IllegalArgumentException("보유한 포인트보다 많은 포인트는 사용할 수 없습니다.");
        }
    }

    public Point subtract(Point usePoint) {
        return new Point(this.point - usePoint.point);
    }

    public Point getNewPoint(Point savePoint, Point usePoint) {
        int newPoint = this.point + savePoint.point - usePoint.point;
        return new Point(newPoint);
    }

    public int point() {
        return point;
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
}

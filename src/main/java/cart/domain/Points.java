package cart.domain;

import cart.exception.OrderException;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Points implements Iterable<Point> {

    List<Point> points;

    public Points(List<Point> points) {
        this.points = points.stream()
                .sorted(Comparator.comparing(Point::getExpiredAt))
                .collect(Collectors.toList());
    }

    public Points getUsablePoints(Point usePoint) {
        validate(usePoint);
        List<Point> usablePoint = new ArrayList<>();
        for (Point point : points) {
            if (usePoint.isEmpty()) {
                break;
            }
            usePoint = addUsablePoint(usePoint, usablePoint, point);
        }
        return new Points(usablePoint);
    }

    private Point addUsablePoint(Point usePoint, List<Point> usablePoint, Point point) {
        if (point.isGreaterThan(usePoint)) {
            Point subtractPoint = point.subtract(usePoint);
            usablePoint.add(point.subtract(subtractPoint));
            return usePoint.subtract(usePoint);
        }
        usablePoint.add(point);
        return usePoint.subtract(point);
    }

    private void validate(Point usePoint) {
        Point totalPoint = Point.from(getTotalPoints());
        if (usePoint.isGreaterThan(totalPoint)) {
            throw new OrderException("사용할 수 있는 포인트보다 더 많은 포인트를 사용했습니다.");
        }
    }

    public int getTotalPoints() {
        return points.stream()
                .mapToInt(Point::getValue)
                .sum();
    }

    public List<Point> getPoints() {
        return points;
    }

    @Override
    public Iterator<Point> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super Point> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<Point> spliterator() {
        return Iterable.super.spliterator();
    }
}

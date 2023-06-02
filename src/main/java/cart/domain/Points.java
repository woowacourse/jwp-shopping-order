package cart.domain;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Points implements Iterable<Point> {

    List<Point> points;

    public Points(List<Point> points) {
        this.points = points;
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

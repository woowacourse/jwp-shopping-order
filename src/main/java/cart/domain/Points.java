package cart.domain;

import java.util.List;

public class Points {

    List<Point> points;

    public Points(List<Point> points) {
        this.points = points;
    }

    public int getPoints() {
        return points.stream()
                .mapToInt(Point::getValue)
                .sum();
    }
}

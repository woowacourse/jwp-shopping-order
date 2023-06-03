package cart.domain;

import java.util.List;

public class Point {

    private final List<PointHistory> pointHistories;

    public Point(List<PointHistory> pointHistories) {
        this.pointHistories = pointHistories;
    }

    public int calculateTotalPoint() {
        return pointHistories.stream()
                .mapToInt(PointHistory::calculatePoint)
                .sum();
    }

    public List<PointHistory> getPointHistories() {
        return pointHistories;
    }

}

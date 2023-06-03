package cart.domain;

import cart.exception.OverFullPointException;

import java.util.List;

public class Point {

    private final List<PointHistory> pointHistories;

    public Point(final List<PointHistory> pointHistories) {
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

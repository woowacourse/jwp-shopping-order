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

    public int applyDisCount(final int totalAmount) {
        final int currentPoint = calculateTotalPoint();
        if (totalAmount < currentPoint) {
            throw new OverFullPointException("포인트는 최대 결제금액만큼 사용할 수 있습니다.");
        }
        return totalAmount - currentPoint;
    }

    public List<PointHistory> getPointHistories() {
        return pointHistories;
    }

}

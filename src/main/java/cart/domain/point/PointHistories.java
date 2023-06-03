package cart.domain.point;

import java.util.List;

public class PointHistories {

    private final List<PointHistory> pointHistories;

    public PointHistories(final List<PointHistory> pointHistories) {
        this.pointHistories = pointHistories;
    }

    public int calculateTotalPoint() {
        return pointHistories.stream()
                .mapToInt(PointHistory::calculatePoint)
                .sum();
    }

    public void validatePointOwnership(final Point point) {
        point.validateOverPoint(calculateTotalPoint());
    }

    public List<PointHistory> getPointHistories() {
        return pointHistories;
    }

}

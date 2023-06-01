package cart.ui.point.dto;

import java.util.List;

public class PointResponse {

    private final List<PointHistoryResponse> pointHistories;
    private final int totalPoint;

    public PointResponse(final List<PointHistoryResponse> pointHistories, final int totalPoint) {
        this.pointHistories = pointHistories;
        this.totalPoint = totalPoint;
    }

    public List<PointHistoryResponse> getPointHistories() {
        return pointHistories;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

}

package cart.ui.point.dto;

import cart.application.service.point.PointResultDto;

import java.util.List;
import java.util.stream.Collectors;

public class PointResponse {

    private final List<PointHistoryResponse> pointHistories;
    private final int totalPoint;

    public PointResponse(List<PointHistoryResponse> pointHistories, int totalPoint) {
        this.pointHistories = pointHistories;
        this.totalPoint = totalPoint;
    }

    public static PointResponse from(PointResultDto pointResultDto) {
        return new PointResponse(pointResultDto.getPointHistoryDtos().stream()
                .map(PointHistoryResponse::from)
                .collect(Collectors.toUnmodifiableList()), pointResultDto.getTotalPoint());
    }

    public List<PointHistoryResponse> getPointHistories() {
        return pointHistories;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

}

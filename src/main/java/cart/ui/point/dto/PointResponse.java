package cart.ui.point.dto;

import cart.application.service.point.dto.PointResultDto;

import java.util.List;
import java.util.stream.Collectors;

public class PointResponse {

    private final List<PointHistoryResponse> pointHistories;
    private final int totalPoint;

    public PointResponse(final List<PointHistoryResponse> pointHistories, final int totalPoint) {
        this.pointHistories = pointHistories;
        this.totalPoint = totalPoint;
    }

    public static PointResponse from(final PointResultDto pointResultDto) {
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

package cart.application.service.point;

import cart.domain.PointHistory;

import java.util.List;
import java.util.stream.Collectors;

public class PointResultDto {

    private final List<PointHistoryDto> pointHistoryDtos;
    private final int totalPoint;

    private PointResultDto(List<PointHistoryDto> pointHistoryDtos, int totalPoint) {
        this.pointHistoryDtos = pointHistoryDtos;
        this.totalPoint = totalPoint;
    }

    public static PointResultDto of(List<PointHistory> pointHistories, int calculateTotalPoint) {
        return new PointResultDto(pointHistories.stream()
                .map(PointHistoryDto::from)
                .collect(Collectors.toUnmodifiableList()), calculateTotalPoint);
    }

    public List<PointHistoryDto> getPointHistoryDtos() {
        return pointHistoryDtos;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

}

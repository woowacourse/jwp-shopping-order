package cart.application.service.point;

import cart.domain.PointHistory;

import java.util.List;
import java.util.stream.Collectors;

public class PointResultDto {

    private final List<PointHistoryDto> pointHistoryDtos;
    private final int totalPoint;

    private PointResultDto(final List<PointHistoryDto> pointHistoryDtos, final int totalPoint) {
        this.pointHistoryDtos = pointHistoryDtos;
        this.totalPoint = totalPoint;
    }

    public static PointResultDto of(final List<PointHistory> pointHistories, final int calculateTotalPoint) {
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

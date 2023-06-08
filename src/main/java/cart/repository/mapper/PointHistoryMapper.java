package cart.repository.mapper;

import cart.dao.dto.point.PointHistoryDto;
import cart.domain.Point;

public class PointHistoryMapper {

    private PointHistoryMapper() {
    }

    public static PointHistoryDto toPointHistoryDto(Point savedPoint, Point usedPoint,
        long orderId, long memberId) {
        return new PointHistoryDto(
            memberId,
            usedPoint.getValue(),
            savedPoint.getValue(),
            orderId
        );
    }

    public static Point toSavedPoint(PointHistoryDto pointHistoryDto) {
        return new Point(
            pointHistoryDto.getPointsSaved()
        );
    }

}

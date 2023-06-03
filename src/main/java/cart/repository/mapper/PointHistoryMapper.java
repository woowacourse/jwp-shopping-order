package cart.repository.mapper;

import cart.dao.entity.PointHistoryEntity;
import cart.domain.Point;

public class PointHistoryMapper {

    private PointHistoryMapper() {
    }

    public static PointHistoryEntity toPointHistoryEntity(Point savedPoint, Point usedPoint,
        long orderId, long memberId) {
        return new PointHistoryEntity(
            memberId,
            usedPoint.getValue(),
            savedPoint.getValue(),
            orderId
        );
    }

    public static Point toSavedPoint(PointHistoryEntity pointHistoryEntity) {
        return new Point(
            pointHistoryEntity.getPointsSaved()
        );
    }

}

package cart.repository.mapper;

import cart.domain.order.Order;
import cart.domain.point.Point;
import cart.entity.PointHistoryEntity;

public class PointHistoryMapper {

    public static PointHistoryEntity toPointHistoryEntity(
            final Order order,
            final Point usedPoint,
            final Point savedPoint
    ) {
        return new PointHistoryEntity(
                order.getMember().getId(),
                usedPoint.getPoint().longValue(),
                savedPoint.getPoint().longValue(),
                order.getId()
        );
    }
}

package cart.Repository.mapper;

import cart.domain.Order.Order;
import cart.domain.Point;
import cart.entity.PointEntity;
import cart.entity.PointHistoryEntity;

public class PointMapper {

    public static PointEntity toPointEntity(Long memberId, Point updatePoint){
        return new PointEntity(
                memberId,
                updatePoint.point()
        );
    }

    public static PointHistoryEntity toPointHistoryEntity(
            Point usePoint,
            Point savePoint,
            Long memberId,
            Long orderId)
    {
        return new PointHistoryEntity(
                memberId,
                usePoint.point(),
                savePoint.point(),
                orderId
        );
    }

}

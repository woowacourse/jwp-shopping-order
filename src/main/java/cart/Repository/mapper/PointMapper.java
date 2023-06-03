package cart.Repository.mapper;

import cart.domain.Order.Order;
import cart.domain.Point;
import cart.entity.PointEntity;
import cart.entity.PointHistoryEntity;

public class PointMapper {

    public static PointEntity toPointEntity(Order order, Point updatePoint){
        return new PointEntity(
                order.getId(),
                updatePoint.point()
        );
    }

    public static PointHistoryEntity toPointHistoryEntity(Order order, Point usePoint, Point savePoint){
        return new PointHistoryEntity(
                order.getMember().getId(),
                usePoint.point(),
                savePoint.point(),
                order.getId()
        );
    }

}

package cart.repository.mapper;

import cart.dao.entity.PointEntity;
import cart.domain.Point;

public class PointMapper {

    private PointMapper() {
    }

    public static PointEntity toPointEntity(Point point, long memberId) {
        return new PointEntity(
            point.getValue(),
            memberId
        );
    }

    public static Point toPoint(PointEntity pointEntity) {
        return new Point(
            pointEntity.getPoint()
        );
    }

}

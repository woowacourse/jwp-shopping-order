package cart.repository.mapper;

import cart.domain.point.Point;
import cart.entity.PointEntity;

public class PointMapper {

    public static Point toPoint(final PointEntity pointEntity) {
        return new Point(pointEntity.getPoint());
    }
}

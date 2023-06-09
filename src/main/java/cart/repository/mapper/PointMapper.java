package cart.repository.mapper;

import cart.domain.point.Point;
import cart.entity.PointEntity;
import java.math.BigDecimal;

public class PointMapper {

    public static Point toPoint(final PointEntity pointEntity) {
        return new Point(BigDecimal.valueOf(pointEntity.getPoint()));
    }
}

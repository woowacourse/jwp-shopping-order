package cart.repository.mapper;

import cart.dao.dto.point.PointDto;
import cart.domain.Point;

public class PointMapper {

    private PointMapper() {
    }

    public static PointDto toPointDto(Point point, long memberId) {
        return new PointDto(
            point.getValue(),
            memberId
        );
    }

    public static Point toPoint(PointDto pointDto) {
        return new Point(
            pointDto.getPoint()
        );
    }

}

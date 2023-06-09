package cart.domain.point;

import java.sql.Timestamp;
import java.util.List;

public interface PointRepository {

    List<Point> findAllAvailablePointsByMemberId(Long memberId, Timestamp boundary);

    Long createPoint(Point point);

    void updateLeftPoint(Point point);
}

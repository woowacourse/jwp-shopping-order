package cart.dao.point;

import cart.domain.point.Point;

import java.sql.Timestamp;
import java.util.List;

public interface PointDao {

    List<Point> findAllAvailablePointsByMemberId(Long memberId, Timestamp boundary);

    Long createPoint(Point point);

    void updateLeftPoint(Point point);
}

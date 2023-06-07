package cart.repository;

import cart.domain.Point;
import cart.domain.Points;

public interface PointRepository {

    Points findUsablePointsByMemberId(Long memberId);

    Point findBy(Long memberId, Long orderId);

    void save(Long memberId, Long orderId, Point point);

    void delete(Long memberId, Long orderId);
}

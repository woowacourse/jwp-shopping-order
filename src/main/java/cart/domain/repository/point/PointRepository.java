package cart.domain.repository.point;

import cart.domain.Point;
import cart.domain.PointHistory;

public interface PointRepository {

    Point findPointByMemberId(Long memberId);

    Long createPointHistory(Long memberId, PointHistory pointHistory);

}

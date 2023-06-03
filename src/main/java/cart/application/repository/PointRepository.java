package cart.application.repository;

import cart.domain.Point;
import cart.domain.PointHistory;

public interface PointRepository {

    Point findPointByMemberId(Long memberId);

    Long createPointHistory(Long memberId, PointHistory pointHistory);

}

package cart.application.repository;

import cart.domain.point.Point;
import cart.domain.point.PointHistory;

public interface PointRepository {

    Point findPointByMemberId(Long memberId);

    Long createPointHistory(final Long memberId, final PointHistory pointHistory);

}

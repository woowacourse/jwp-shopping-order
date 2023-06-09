package cart.application.repository.point;

import cart.domain.point.PointHistories;
import cart.domain.point.PointHistory;

public interface PointRepository {

    PointHistories findPointByMemberId(Long memberId);

    Long createPointHistory(final Long memberId, final PointHistory pointHistory);

}

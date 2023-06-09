package cart.domain.repository;

import cart.domain.Member;
import cart.domain.Point;

public interface PointRepository {

    Point findPointByMemberId(Long memberId);

}

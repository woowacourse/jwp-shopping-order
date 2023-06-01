package cart.application.repository;

import cart.domain.Member;
import cart.domain.Point;

public interface PointRepository {
    Point findPointByMember(Member member);
}

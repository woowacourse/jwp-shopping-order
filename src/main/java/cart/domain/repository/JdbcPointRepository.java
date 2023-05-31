package cart.domain.repository;

import cart.dao.MemberDao;
import cart.domain.Point;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcPointRepository implements PointRepository{

    private final MemberDao memberDao;

    public JdbcPointRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Point findPointByMemberId(final Long memberId) {
        return memberDao.getPointByMemberId(memberId);
    }
}

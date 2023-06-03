package cart.repository;

import cart.dao.PointDao;
import cart.domain.member.Member;
import cart.domain.point.Point;
import cart.exception.PointException;
import cart.repository.mapper.PointMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PointRepository {

    private final PointDao pointDao;

    public PointRepository(final PointDao pointDao) {
        this.pointDao = pointDao;
    }

    public void updateMemberPoint(final Member member, final Point point) {
        pointDao.update(member.getId(), point.getPoint());
    }

    public Point findPointByMember(final Member member) {
        return PointMapper.toPoint(pointDao.findByMemberId(member.getId()).orElseThrow(
                () -> new PointException.NotFound(member)
        ));
    }
}

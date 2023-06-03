package cart.repository;

import cart.dao.PointDao;
import cart.dao.entity.PointEntity;
import cart.domain.Point;
import cart.exception.AuthenticationException;
import cart.repository.mapper.PointMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PointRepository {

    private final PointDao pointDao;

    public PointRepository(PointDao pointDao) {
        this.pointDao = pointDao;
    }

    public void update(Point point, long memberId) {
        pointDao.update(PointMapper.toPointEntity(point, memberId));
    }

    public Point findByMemberId(long memberId) {
        PointEntity pointEntity = pointDao.findByMemberId(memberId)
            .orElseThrow(AuthenticationException::new);
        return PointMapper.toPoint(pointEntity);
    }
}

package cart.repository;

import cart.dao.PointDao;
import cart.dao.entity.PointEntity;
import cart.domain.Point;
import org.springframework.stereotype.Repository;

@Repository
public class PointRepository {

    private final PointDao pointDao;

    public PointRepository(PointDao pointDao) {
        this.pointDao = pointDao;
    }

    public Point findPointByMemberId(Long memberId) {
        PointEntity pointEntity = pointDao.findPointByMemberId(memberId).get();
        return new Point(pointEntity.getPoint());
    }

    public void updatePoint(Long memberId, Long point) {
        pointDao.updatePoint(memberId, point);
    }
}

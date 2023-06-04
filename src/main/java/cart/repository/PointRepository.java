package cart.repository;

import cart.dao.PointDao;
import org.springframework.stereotype.Repository;

@Repository
public class PointRepository {

    private final PointDao pointDao;

    public PointRepository(PointDao pointDao) {
        this.pointDao = pointDao;
    }

    public Long findPointByMemberId(Long id) {
        return pointDao.findPointByMemberId(id).get().getPoint();
    }
}

package cart.repository;

import cart.dao.PointDao;
import cart.dao.PointHistoryDao;
import cart.dao.entity.PointEntity;
import cart.dao.entity.PointHistoryEntity;
import cart.domain.Point;
import cart.exception.AuthenticationException;
import cart.exception.OrderNotFoundException;
import cart.repository.mapper.PointHistoryMapper;
import cart.repository.mapper.PointMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PointRepository {

    private final PointDao pointDao;
    private final PointHistoryDao pointHistoryDao;

    public PointRepository(PointDao pointDao, PointHistoryDao pointHistoryDao) {
        this.pointDao = pointDao;
        this.pointHistoryDao = pointHistoryDao;
    }

    public void update(Point point, long memberId) {
        pointDao.update(PointMapper.toPointEntity(point, memberId));
    }

    public Point findByMemberId(long memberId) {
        PointEntity pointEntity = pointDao.findByMemberId(memberId)
            .orElseThrow(AuthenticationException::new);
        return PointMapper.toPoint(pointEntity);
    }

    public void savePointHistory(Point pointToSave, Point usedPoint, long orderId, long memberId) {
        PointHistoryEntity pointHistoryEntity = PointHistoryMapper.toPointHistoryEntity(
            pointToSave, usedPoint, orderId, memberId);
        pointHistoryDao.save(pointHistoryEntity);
    }

    public Point findSavedPointByOrderId(long orderId) {
        PointHistoryEntity pointHistoryEntity = pointHistoryDao.findByOrderId(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));
        return PointHistoryMapper.toSavedPoint(pointHistoryEntity);
    }
}

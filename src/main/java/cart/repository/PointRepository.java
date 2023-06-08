package cart.repository;

import cart.dao.PointDao;
import cart.dao.PointHistoryDao;
import cart.dao.dto.point.PointDto;
import cart.dao.dto.point.PointHistoryDto;
import cart.domain.Point;
import cart.exception.authexception.AuthenticationException;
import cart.exception.notfoundexception.OrderNotFoundException;
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
        pointDao.update(PointMapper.toPointDto(point, memberId));
    }

    public Point findByMemberId(long memberId) {
        PointDto pointDto = pointDao.findByMemberId(memberId)
            .orElseThrow(AuthenticationException::new);
        return PointMapper.toPoint(pointDto);
    }

    public void savePointHistory(Point pointToSave, Point usedPoint, long orderId, long memberId) {
        PointHistoryDto pointHistoryDto = PointHistoryMapper.toPointHistoryDto(
            pointToSave, usedPoint, orderId, memberId);
        pointHistoryDao.save(pointHistoryDto);
    }

    public Point findSavedPointByOrderId(long orderId) {
        PointHistoryDto pointHistoryDto = pointHistoryDao.findByOrderId(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));
        return PointHistoryMapper.toSavedPoint(pointHistoryDto);
    }
}

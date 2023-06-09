package cart.repository;

import cart.dao.PointDao;
import cart.dao.PointHistoryDao;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.point.Point;
import cart.entity.PointHistoryEntity;
import cart.exception.PointException;
import cart.exception.PointHistoryException;
import cart.repository.mapper.PointHistoryMapper;
import cart.repository.mapper.PointMapper;
import java.math.BigDecimal;
import org.springframework.stereotype.Repository;

@Repository
public class PointRepository {

    private final PointDao pointDao;
    private final PointHistoryDao pointHistoryDao;

    public PointRepository(final PointDao pointDao, final PointHistoryDao pointHistoryDao) {
        this.pointDao = pointDao;
        this.pointHistoryDao = pointHistoryDao;
    }

    public void updateMemberPoint(final Member member, final Point point) {
        pointDao.update(member.getId(), point.getPoint().longValue());
    }

    public Point findPointByMember(final Member member) {
        return PointMapper.toPoint(pointDao.findByMemberId(member.getId()).orElseThrow(
                () -> new PointException.NotFound(member)
        ));
    }

    public void updatePointHistory(
            final Order order,
            final Point usedPoint,
            final Point savedPoint
    ) {
        pointHistoryDao.save(PointHistoryMapper.toPointHistoryEntity(order, usedPoint, savedPoint));
    }

    public Point findPointSavedHistory(final Long orderId) {
        final PointHistoryEntity pointHistoryEntity = pointHistoryDao.findByOrderId(orderId).orElseThrow(
                () -> new PointHistoryException.NotFound(orderId)
        );

        return new Point(BigDecimal.valueOf(pointHistoryEntity.getPointsSaved()));
    }
}

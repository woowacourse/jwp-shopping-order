package cart.repository;

import java.sql.Timestamp;
import java.util.List;

import cart.dao.PointDao;
import cart.domain.Member;
import cart.domain.OrderPoint;
import cart.domain.Point;
import cart.domain.PointManager;
import cart.domain.Price;
import cart.entity.PointEntity;
import cart.exception.NotEnoughPointException;
import org.springframework.stereotype.Repository;

@Repository
public class PointRepository {

    private final PointDao pointDao;
    private final PointManager pointManager;

    public PointRepository(final PointDao pointDao, final PointManager pointManager) {
        this.pointDao = pointDao;
        this.pointManager = pointManager;
    }

    public OrderPoint updatePoint(final Member member, final Point usedPoint, final Price totalPrice, final Timestamp createdAt) {
        reduceMemberPoint(member, usedPoint);
        final PointEntity pointEntity = addPoint(member, totalPrice, createdAt);
        return new OrderPoint(pointEntity.getId(), usedPoint, Point.valueOf(pointEntity.getEarnedPoint()));
    }

    private void reduceMemberPoint(final Member member, final Point usedPoint) {
        final List<PointEntity> pointEntities = pointDao.findRemainingPointsByMemberId(member.getId());
        final Point totalLeftPoint = getTotalLeftPoint(pointEntities);
        validatePoint(totalLeftPoint, usedPoint);
        deleteLeftPointByUsedPoint(pointEntities, usedPoint, 0);
    }

    private Point getTotalLeftPoint(final List<PointEntity> pointEntities) {
        return Point.valueOf(pointEntities.stream()
                .mapToInt(PointEntity::getLeftPoint)
                .sum());
    }

    private void validatePoint(final Point totalLeftPoint, final Point usedPoint) {
        if (totalLeftPoint.isLessThan(usedPoint)) {
            throw new NotEnoughPointException(totalLeftPoint.getValue(), usedPoint.getValue());
        }
    }

    private void deleteLeftPointByUsedPoint(final List<PointEntity> pointEntities, final Point usedPoint, final int index) {
        if (usedPoint.equals(Point.ZERO)) {
            return;
        }
        final PointEntity pointEntity = pointEntities.get(index);
        final Point leftPoint = Point.valueOf(pointEntity.getLeftPoint());
        if (usedPoint.isLessThan(leftPoint)) {
            pointDao.updateLeftPoint(pointEntity.getId(), leftPoint.subtract(usedPoint));
            return;
        }
        pointDao.updateLeftPoint(pointEntity.getId(), Point.ZERO);
        deleteLeftPointByUsedPoint(pointEntities, usedPoint.subtract(leftPoint), index + 1);
    }

    private PointEntity addPoint(final Member member, final Price totalPrice, final Timestamp createdAt) {
        final Point earnedPoint = pointManager.getPoint(totalPrice);
        final Timestamp expiredAt = pointManager.getExpiredAt(createdAt);
        return pointDao.insert(new PointEntity(earnedPoint.getValue(), earnedPoint.getValue(), member.getId(), expiredAt, createdAt));
    }
}

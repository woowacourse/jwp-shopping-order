package cart.repository;

import cart.dao.PointDao;
import cart.dao.PointHistoryDao;
import cart.domain.Point;
import cart.domain.Points;
import cart.entity.PointEntity;
import cart.entity.PointHistoryEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PointRepository {

    private static final int INIT_USED_POINT = 0;

    private final PointDao pointDao;
    private final PointHistoryDao pointHistoryDao;

    public PointRepository(PointDao pointDao, PointHistoryDao pointHistoryDao) {
        this.pointDao = pointDao;
        this.pointHistoryDao = pointHistoryDao;
    }

    public Points findUsablePointsByMemberId(Long memberId) {
        List<PointEntity> pointEntities = pointDao.findByMemberId(memberId);
        List<PointEntity> pointsNotExpired = getPointsNotExpired(pointEntities);
        List<PointHistoryEntity> pointHistoryEntities = getPointHistoryEntities(pointsNotExpired);

        Map<Long, Integer> usedPointById = calculateUsedPointById(pointsNotExpired, pointHistoryEntities);

        return generatePoints(pointsNotExpired, usedPointById);
    }

    private List<PointEntity> getPointsNotExpired(List<PointEntity> pointEntities) {
        return pointEntities.stream()
                .filter(this::isNotExpiredPoint)
                .sorted(Comparator.comparing(PointEntity::getExpiredAt))
                .collect(Collectors.toList());
    }

    private boolean isNotExpiredPoint(PointEntity pointEntity) {
        return LocalDate.now().isBefore(pointEntity.getExpiredAt()) || LocalDate.now().isEqual(pointEntity.getExpiredAt());
    }

    private List<PointHistoryEntity> getPointHistoryEntities(List<PointEntity> pointsNotExpired) {
        return pointHistoryDao.findByPointIds(pointsNotExpired.stream()
                .map(PointEntity::getId)
                .collect(Collectors.toList()));
    }

    private Map<Long, Integer> calculateUsedPointById(List<PointEntity> pointsNotExpired, List<PointHistoryEntity> pointHistoryEntities) {
        Map<Long, Integer> usedPointById = new HashMap<>();
        for (PointEntity pointEntity : pointsNotExpired) {
            usedPointById.put(pointEntity.getId(), INIT_USED_POINT);
        }

        for (PointHistoryEntity pointHistoryEntity : pointHistoryEntities) {
            Long pointId = pointHistoryEntity.getPointId();
            int usedPoint = pointHistoryEntity.getUsedPoint();

            usedPointById.put(pointId, usedPoint + usedPointById.get(pointId));
        }
        return usedPointById;
    }

    private Points generatePoints(List<PointEntity> pointsNotExpired, Map<Long, Integer> usedPointById) {
        List<Point> points = new ArrayList<>();
        for (PointEntity pointEntity : pointsNotExpired) {
            Long id = pointEntity.getId();
            int value = pointEntity.getValue() - usedPointById.get(id);
            String comment = pointEntity.getComment();
            LocalDate createAt = pointEntity.getCreateAt();
            LocalDate expiredAt = pointEntity.getExpiredAt();

            points.add(Point.of(id, value, comment, createAt, expiredAt));
        }
        return new Points(points);
    }

    public void save(Long memberId, Long orderId, Point point) {
        PointEntity pointEntity = new PointEntity(point.getId(), point.getValue(), point.getComment(), point.getCreateAt(), point.getExpiredAt());
        pointDao.save(memberId, orderId, pointEntity);
    }

    public void delete(Long orderId) {
        pointDao.deleteByOrderId(orderId);
    }
}

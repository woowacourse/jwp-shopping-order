package cart.repository;

import cart.dao.PointDao;
import cart.dao.PointHistoryDao;
import cart.domain.Point;
import cart.domain.Points;
import cart.entity.PointEntity;
import cart.entity.PointHistoryEntity;
import cart.exception.OrderException;
import cart.exception.OrderServerException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PointRepository {

    private static final int INIT_USED_POINT = 0;
    private static final String INVALID_USE_POINT_MESSAGE = "사용된 포인트를 취소할 수 없습니다.";
    private static final String NO_POINT_MESSAGE = "입력한 포인트가 없습니다.";
    private static final String INVALID_SAVE_POINT_MESSAGE = "데이터베이스에 포인트를 적립할 수 없습니다.";

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
        System.out.println(pointEntities);
        return pointEntities.stream()
                .filter(this::isNotExpiredPoint)
                .collect(Collectors.toList());
    }

    private boolean isNotExpiredPoint(PointEntity pointEntity) {
        return LocalDate.now().isBefore(pointEntity.getExpiredAt()) || LocalDate.now().isEqual(pointEntity.getExpiredAt());
    }

    private List<PointHistoryEntity> getPointHistoryEntities(List<PointEntity> pointsNotExpired) {
        System.out.println(pointsNotExpired);
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

    public Point findBy(Long memberId, Long orderId) {
        try {
            PointEntity pointEntity = pointDao.findBy(memberId, orderId);
            Long id = pointEntity.getId();
            int value = pointEntity.getValue();
            String comment = pointEntity.getComment();
            LocalDate createAt = pointEntity.getCreateAt();
            LocalDate expiredAt = pointEntity.getExpiredAt();
            return Point.of(id, value, comment, createAt, expiredAt);
        } catch (DataAccessException exception) {
            throw new OrderException(NO_POINT_MESSAGE);
        }
    }

    public void save(Long memberId, Long orderId, Point point) {
        try {
            PointEntity pointEntity = new PointEntity(point.getId(), point.getValue(), point.getComment(), point.getCreateAt(), point.getExpiredAt());
            pointDao.save(memberId, orderId, pointEntity);
        } catch (DataAccessException exception) {
            throw new OrderServerException(INVALID_SAVE_POINT_MESSAGE);
        }
    }

    public void delete(Long memberId, Long orderId) {
        validateDelete(memberId, orderId);
        pointDao.delete(memberId, orderId);
        pointHistoryDao.deleteByOrderId(orderId);
    }

    private void validateDelete(Long memberId, Long orderId) {
        PointEntity pointEntity;
        try {
            pointEntity = pointDao.findBy(memberId, orderId);
        } catch (DataAccessException exception) {
            throw new OrderException(NO_POINT_MESSAGE);
        }
        if (isUsedPoint(pointEntity)) {
            throw new OrderException(INVALID_USE_POINT_MESSAGE);
        }
    }

    private boolean isUsedPoint(PointEntity pointEntity) {
        return pointHistoryDao.isIn(pointEntity.getId());
    }
}

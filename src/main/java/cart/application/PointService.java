package cart.application;

import cart.domain.member.Member;
import cart.domain.point.Point;
import cart.domain.point.PointRepository;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointService {

    private static final Logger logger = LoggerFactory.getLogger(PointService.class);

    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Transactional(propagation = Propagation.NESTED)
    public Long createPoint(Point point) {
        try {
            return pointRepository.createPoint(point);
        } catch (DataAccessException e) {
            logger.error("포인트적립 예외 발생 id: {}, point: {}, createdAt: {}", point.getMember().getId(), point.getEarnedPoint(), point.getCreatedAt(), e);
            return 0L;
        }
    }

    @Transactional
    public void usePoint(Member member, Timestamp createdAt, Long usePoint) {
        List<Point> points = pointRepository.findAllAvailablePointsByMemberId(member.getId(), createdAt);
        long availablePoints = points.stream()
                .mapToLong(Point::getLeftPoint)
                .sum();

        if (availablePoints < usePoint) {
            throw new CartException(ErrorCode.POINT_NOT_ENOUGH);
        }

        List<Point> updatePoints = points.stream()
                .sorted(Comparator.comparing(Point::getExpiredAt))
                .collect(Collectors.toList());
        for (Point point : updatePoints) {
            usePoint = usePointAndReturnRestPoint(point, usePoint);
            pointRepository.updateLeftPoint(point);
        }
    }

    private long usePointAndReturnRestPoint(Point point, long usedPoint) {
        Long leftPoint = point.getLeftPoint();
        if (usedPoint > leftPoint) {
            point.usePoint(leftPoint);
            return usedPoint - leftPoint;
        }
        point.usePoint(usedPoint);
        return 0;
    }
}

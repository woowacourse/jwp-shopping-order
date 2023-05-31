package cart.application;

import cart.dao.PointDao;
import cart.domain.ExpiredCategory;
import cart.domain.Member;
import cart.domain.Point;
import cart.exception.NotEnoughPointException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PointService {

    private static final double MEMBER_ORDER_RATE = 0.01;
    private final PointDao pointDao;

    public PointService(PointDao pointDao) {
        this.pointDao = pointDao;
    }

    public Point savePointByMember(Member member, int amount, LocalDateTime issueDateTime) {
        int earnedPoint = (int) (amount * findEarnRateByMember(member));
        Point point = new Point(earnedPoint, earnedPoint, member, issueDateTime, issueDateTime.plusDays(ExpiredCategory.ORDER.getValue()));
        Long pointId = createPoint(point);
        return new Point(pointId, point.getEarnedPoint(), point.getLeftPoint(), point.getMember(), point.getExpiredAt(), point.getCreatedAt());
    }

    public double findEarnRateByMember(Member member) {
        return MEMBER_ORDER_RATE;
    }

    public int findByMember(Member member) {
        List<Point> remainingPoints = pointDao.getBeforeExpirationAndRemainingPointsByMemberId(member.getId());
        return remainingPoints.stream().mapToInt(Point::getLeftPoint).sum();
    }

    public Long createPoint(Point point) {
        return pointDao.createPoint(point);
    }

    public void usePointByMember(Member member, int usingPoint) {
        List<Point> remainingPoints = pointDao.getBeforeExpirationAndRemainingPointsByMemberId(member.getId());
        int balance = remainingPoints.stream().mapToInt(Point::getLeftPoint).sum();
        if (balance < usingPoint) {
            throw new NotEnoughPointException();
        }

        if (usingPoint == balance) {
            for (Point point : remainingPoints) {
                pointDao.updatePointLeftBalance(point, 0);
            }
            return;
        }

        for (Point point : remainingPoints) {
            if (usingPoint == 0) {
                return;
            }
            if (usingPoint >= point.getLeftPoint()) {
                usingPoint -= point.getLeftPoint();
                pointDao.updatePointLeftBalance(point, 0);
                continue;
            }
            if (usingPoint < point.getLeftPoint()) {
                pointDao.updatePointLeftBalance(point, point.getLeftPoint() - usingPoint);
                usingPoint = 0;
            }
        }
    }
}

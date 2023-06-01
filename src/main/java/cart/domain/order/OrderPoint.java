package cart.domain.order;

import cart.domain.member.Member;
import cart.domain.point.Point;
import cart.domain.point.PointPolicy;
import cart.exception.customexception.PointExceedTotalPriceException;
import cart.exception.customexception.PointNotEnoughException;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderPoint {

    private PointPolicy pointPolicy;

    public OrderPoint(PointPolicy pointPolicy) {
        this.pointPolicy = pointPolicy;
    }

    public Point earnPoint(Member member, long usedPoint, long totalPrice, Timestamp createdAt) {
        if (totalPrice < usedPoint) {
            throw new PointExceedTotalPriceException();
        }
        long earnedPoint = pointPolicy.calculateEarnedPoint(member, totalPrice - usedPoint);
        Timestamp expiredAt = pointPolicy.calculateExpiredAt(createdAt);
        return new Point(earnedPoint, earnedPoint, member, expiredAt, createdAt);
    }

    public List<Point> usePoint(long usedPoint, List<Point> points) {
        long availablePoints = points.stream()
                .mapToLong(Point::getLeftPoint)
                .sum();
        if (availablePoints < usedPoint) {
            throw new PointNotEnoughException();
        }
        List<Point> sortedPoints = points.stream()
                .sorted(Comparator.comparing(Point::getExpiredAt))
                .collect(Collectors.toList());

        for (Point point : sortedPoints) {
            usedPoint = usePointAndReturnRestPoint(point, usedPoint);
        }
        return sortedPoints;
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

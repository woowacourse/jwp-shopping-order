package cart.domain.point;

import cart.exception.PointException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PointPolicy {

    private PointPolicy() {}

    public static List<UsedPoint> usedPoint(MemberPoints memberPoints, int usedPoint) {
        canUsedPoint(memberPoints, usedPoint);
        return getUsedPoints(memberPoints, usedPoint);
    }

    private static void canUsedPoint(MemberPoints memberPoints, int usedPoint) {
        if (memberPoints.getUsablePoints() < usedPoint) {
            throw new PointException("사용하려는 포인트가 부족합니다.");
        }
    }

    private static List<UsedPoint> getUsedPoints(MemberPoints memberPoints, int usedPointAmount) {
        sortByExpiredAt(memberPoints.getPoints());
        List<UsedPoint> usedPoints = new ArrayList<>();
        for (Point point : memberPoints.getPoints()) {
            UsedPoint usedPoint = getUsedPoint(point, usedPointAmount);
            usedPointAmount -= usedPoint.getUsedPoint();
            usedPoints.add(usedPoint);
            if (usedPointAmount <= 0) {
                break;
            }
        }
        return usedPoints;
    }

    private static void sortByExpiredAt(List<Point> points) {
        points.sort(Comparator.comparing(Point::getExpiredAt));
    }

    private static UsedPoint getUsedPoint(Point point, int usedPoint) {
        int pointAmount = point.calculatePointByExpired();
        if (pointAmount <= usedPoint) {
            point.usedAllPoint();
            return new UsedPoint(point.getId(), pointAmount);
        }

        point.decreasePoint(usedPoint);
        return new UsedPoint(point.getId(), usedPoint);
    }
}

package cart.domain.point;

import cart.domain.Member;
import cart.exception.PointException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MemberPoints {

    private final Member member;
    private final List<Point> points;

    public MemberPoints(final Member member, final List<Point> points) {
        this.member = member;
        this.points = new ArrayList<>(points);
    }

    public List<UsedPoint> usedPoint(int usedPoint) {
        canUsedPoint(usedPoint);
        return getUsedPoints(usedPoint);
    }

    private void canUsedPoint(int usedPoint) {
        if (getUsablePoints() < usedPoint) {
            throw new PointException("사용하려는 포인트가 부족합니다.");
        }
    }

    // TODO: 2023/06/01 indent 1 줄이기
    private List<UsedPoint> getUsedPoints(int usedPointAmount) {
        sortByExpiredAt();
        List<UsedPoint> usedPoints = new ArrayList<>();
        for (Point point : points) {
            UsedPoint usedPoint = getUsedPoint(point, usedPointAmount);
            usedPointAmount -= usedPoint.getUsedPoint();
            usedPoints.add(usedPoint);
            if (usedPointAmount <= 0) {
                break;
            }
        }
        return usedPoints;
    }

    private void sortByExpiredAt() {
        points.sort(Comparator.comparing(Point::getExpiredAt));
    }

    private UsedPoint getUsedPoint(Point point, int usedPoint) {
        int pointAmount = point.getPointAmount();
        if (pointAmount <= usedPoint) {
            point.usedAllPoint();
            return new UsedPoint(point.getId(), pointAmount);
        }

        point.decreasePoint(usedPoint);
        return new UsedPoint(point.getId(), usedPoint);
    }

    public void cancelledPoints(List<UsedPoint> usedPoints) {
        for (UsedPoint usedPoint : usedPoints) {
            Point point = getPointById(usedPoint.getId());
            point.increasePoint(usedPoint.getUsedPoint());
        }
    }

    private Point getPointById(Long id) {
        return points.stream()
                     .filter(point -> point.isMatchId(id))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("해당 적립 포인트를 찾을 수 없습니다."));
    }

    public int getUsablePoints() {
        return points.stream()
                     .mapToInt(Point::getPointAmount)
                     .sum();
    }

    public int getToBeExpiredPoints() {
        LocalDateTime now = LocalDateTime.now();
        return points.stream()
                     .filter(point -> point.isToBeExpired(now))
                     .mapToInt(Point::getPointAmount)
                     .sum();
    }

    public Member getMember() {
        return member;
    }

    public List<Point> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "MemberPoints{" +
                "member=" + member +
                ", points=" + points +
                '}';
    }
}

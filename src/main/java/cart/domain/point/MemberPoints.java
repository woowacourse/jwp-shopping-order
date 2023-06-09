package cart.domain.point;

import cart.domain.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MemberPoints {

    private final Member member;
    private final List<Point> points;

    public MemberPoints(final Member member, final List<Point> points) {
        this.member = member;
        this.points = new ArrayList<>(points);
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
                     .mapToInt(Point::calculatePointByExpired)
                     .sum();
    }

    public int getToBeExpiredPoints() {
        LocalDateTime now = LocalDateTime.now();
        return points.stream()
                     .filter(point -> point.isToBeExpired(now))
                     .mapToInt(Point::calculatePointByExpired)
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

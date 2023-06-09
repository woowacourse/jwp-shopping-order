package cart.domain.point;

import cart.domain.member.Member;

import java.sql.Timestamp;
import java.util.Objects;

public class Point {

    private Long id;
    private final Long earnedPoint;
    private Long leftPoint;
    private final Member member;
    private final Timestamp expiredAt;
    private final Timestamp createdAt;

    public Point(Long earnedPoint, Long leftPoint, Member member, Timestamp expiredAt, Timestamp createdAt) {
        this.earnedPoint = earnedPoint;
        this.leftPoint = leftPoint;
        this.member = member;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }

    public Point(Long id, Long earnedPoint, Long leftPoint, Member member, Timestamp expiredAt, Timestamp createdAt) {
        this.id = id;
        this.earnedPoint = earnedPoint;
        this.leftPoint = leftPoint;
        this.member = member;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }

    public void usePoint(long usePoint) {
        if (leftPoint.equals(0L)) {
            return;
        }
        if (leftPoint < usePoint) {
            leftPoint = 0L;
            return;
        }
        leftPoint = leftPoint - usePoint;
    }

    public Long getId() {
        return id;
    }

    public Long getEarnedPoint() {
        return earnedPoint;
    }

    public Long getLeftPoint() {
        return leftPoint;
    }

    public Member getMember() {
        return member;
    }

    public Timestamp getExpiredAt() {
        return expiredAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(id, point.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

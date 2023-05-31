package cart.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Point {
    private Long id;
    private final PointVO earnedPoint;
    private PointVO leftPoint;
    private final Member member;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;

    public Point(int earnedPoint, int leftPoint, Member member, LocalDateTime expiredAt, LocalDateTime createdAt) {
        this(null, earnedPoint, leftPoint, member, expiredAt, createdAt);
    }

    public Point(Long id, int earnedPoint, int leftPoint, Member member, LocalDateTime expiredAt, LocalDateTime createdAt) {
        this.id = id;
        validatePointAmount(earnedPoint, leftPoint);
        this.earnedPoint = new PointVO(earnedPoint);
        this.leftPoint = new PointVO(leftPoint);
        this.member = member;
        validateTime(expiredAt, createdAt);
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }

    private void validateTime(LocalDateTime expiredAt, LocalDateTime createdAt) {
        if (createdAt.isAfter(expiredAt)) {
            throw new IllegalArgumentException("유효시각이 만료시각 이전이어야 합니다.");
        }
    }

    private void validatePointAmount(int earnedPoint, int leftPoint) {
        if (earnedPoint < leftPoint) {
            throw new IllegalArgumentException("잔여 포인트는 발급 포인트보다 많을 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public int getEarnedPoint() {
        return earnedPoint.getValue();
    }

    public int getLeftPoint() {
        return leftPoint.getValue();
    }

    public Member getMember() {
        return member;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return Objects.equals(id, point.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package cart.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Point {
    private Long id;
    private int earnedPoint;
    private int leftPoint;
    private final Member member;
    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;

    public Point(int earnedPoint, int leftPoint, Member member, LocalDateTime expiredAt, LocalDateTime createdAt) {
        this.earnedPoint = earnedPoint;
        this.leftPoint = leftPoint;
        this.member = member;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }

    public Point(Long id, int earnedPoint, int leftPoint, Member member, LocalDateTime expiredAt, LocalDateTime createdAt) {
        this.id = id;
        this.earnedPoint = earnedPoint;
        this.leftPoint = leftPoint;
        this.member = member;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public int getEarnedPoint() {
        return earnedPoint;
    }

    public int getLeftPoint() {
        return leftPoint;
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

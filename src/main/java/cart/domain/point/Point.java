package cart.domain.point;

import java.time.LocalDateTime;

public class Point {

    private Long id;
    private Integer pointAmount;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiredAt;

    public Point(final Integer pointAmount,
                 final LocalDateTime createdAt, final LocalDateTime expiredAt) {
        this.pointAmount = pointAmount;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
    }

    public Point(final Long id, final Integer pointAmount,
                 final LocalDateTime createdAt, final LocalDateTime expiredAt) {
        this.id = id;
        this.pointAmount = pointAmount;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
    }

    public void usedAllPoint() {
        this.pointAmount = 0;
    }

    public void decreasePoint(int point) {
        this.pointAmount -= point;
    }

    public void increasePoint(int point) {
        this.pointAmount += point;
    }

    public Long getId() {
        return id;
    }

    public Integer getPointAmount() {
        return pointAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", pointAmount=" + pointAmount +
                ", createdAt=" + createdAt +
                ", expiredAt=" + expiredAt +
                '}';
    }
}

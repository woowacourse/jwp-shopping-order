package cart.entity;

import java.sql.Timestamp;

public class PointEntity {

    private final Long id;
    private final int earnedPoint;
    private final int leftPoint;
    private final Long memberId;
    private final Timestamp expiredAt;
    private final Timestamp createdAt;

    public PointEntity(final Long id, final PointEntity other) {
        this(id, other.earnedPoint, other.leftPoint, other.memberId, other.expiredAt, other.createdAt);
    }

    public PointEntity(final int earnedPoint, final int leftPoint, final Long memberId, final Timestamp expiredAt, final Timestamp createdAt) {
        this(null, earnedPoint, leftPoint, memberId, expiredAt, createdAt);
    }

    public PointEntity(final Long id, final int earnedPoint, final int leftPoint, final Long memberId, final Timestamp expiredAt, final Timestamp createdAt) {
        this.id = id;
        this.earnedPoint = earnedPoint;
        this.leftPoint = leftPoint;
        this.memberId = memberId;
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

    public Long getMemberId() {
        return memberId;
    }

    public Timestamp getExpiredAt() {
        return expiredAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}

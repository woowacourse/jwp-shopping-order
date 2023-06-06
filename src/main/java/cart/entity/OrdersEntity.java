package cart.entity;

import java.sql.Timestamp;

public class OrdersEntity {

    private final Long id;
    private final Long memberId;
    private final Long pointId;
    private final int earnedPoint;
    private final int usedPoint;
    private final Timestamp createdAt;

    public OrdersEntity(final Long id, final OrdersEntity other) {
        this(id, other.memberId, other.pointId, other.earnedPoint, other.usedPoint, other.createdAt);
    }

    public OrdersEntity(final Long memberId, final Long pointId, final int earnedPoint, final int usedPoint, final Timestamp createdAt) {
        this(null, memberId, pointId, earnedPoint, usedPoint, createdAt);
    }

    public OrdersEntity(
            final Long id,
            final Long memberId,
            final Long pointId,
            final int earnedPoint,
            final int usedPoint,
            final Timestamp createdAt
    ) {
        this.id = id;
        this.memberId = memberId;
        this.pointId = pointId;
        this.earnedPoint = earnedPoint;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getPointId() {
        return pointId;
    }

    public int getEarnedPoint() {
        return earnedPoint;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}

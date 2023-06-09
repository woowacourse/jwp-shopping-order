package cart.entity;

public class PointHistoryEntity {
    private final Long id;
    private final Long memberId;
    private final Long pointsUsed;
    private final Long pointsSaved;
    private final Long orderId;

    public PointHistoryEntity(
            final Long memberId,
            final Long pointsUsed,
            final Long pointsSaved,
            final Long orderId
    ) {
        this(null,
                memberId,
                pointsUsed,
                pointsSaved,
                orderId
        );
    }

    public PointHistoryEntity(
            final Long id,
            final Long memberId,
            final Long pointsUsed,
            final Long pointsSaved,
            final Long orderId
    ) {
        this.id = id;
        this.memberId = memberId;
        this.pointsUsed = pointsUsed;
        this.pointsSaved = pointsSaved;
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getPointsUsed() {
        return pointsUsed;
    }

    public Long getPointsSaved() {
        return pointsSaved;
    }

    public Long getOrderId() {
        return orderId;
    }
}

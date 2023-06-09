package cart.entity;

public class PointHistoryEntity {
    private final Long memberId;
    private final int pointUsed;
    private final int pointSaved;
    private final Long orderId;

    public PointHistoryEntity(Long memberId, int point_used, int point_saved, Long orderId) {
        this.memberId = memberId;
        this.pointUsed = point_used;
        this.pointSaved = point_saved;
        this.orderId = orderId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getPointUsed() {
        return pointUsed;
    }

    public int getPointSaved() {
        return pointSaved;
    }

    public Long getOrderId() {
        return orderId;
    }
}

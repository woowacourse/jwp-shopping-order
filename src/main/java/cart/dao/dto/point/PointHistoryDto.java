package cart.dao.dto.point;

public class PointHistoryDto {

    private final Long id;
    private final long memberId;
    private final int pointsUsed;
    private final int pointsSaved;
    private final long orderId;

    public PointHistoryDto(long memberId, int pointsUsed, int pointsSaved, long orderId) {
        this(null, memberId, pointsUsed, pointsSaved, orderId);
    }

    public PointHistoryDto(Long id, long memberId, int pointsUsed, int pointsSaved, long orderId) {
        this.id = id;
        this.memberId = memberId;
        this.pointsUsed = pointsUsed;
        this.pointsSaved = pointsSaved;
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public int getPointsUsed() {
        return pointsUsed;
    }

    public int getPointsSaved() {
        return pointsSaved;
    }

    public long getOrderId() {
        return orderId;
    }
}

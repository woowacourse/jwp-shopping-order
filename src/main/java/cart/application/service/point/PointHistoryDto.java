package cart.application.service.point;

import cart.domain.PointHistory;

public class PointHistoryDto {

    private final long orderId;
    private final int earnedPoint;
    private final int usedPoint;

    private PointHistoryDto(long orderId, int earnedPoint, int usedPoint) {
        this.orderId = orderId;
        this.earnedPoint = earnedPoint;
        this.usedPoint = usedPoint;
    }

    public static PointHistoryDto from(PointHistory pointHistory) {
        return new PointHistoryDto(pointHistory.getOrderId(), pointHistory.getEarnedPoint(), pointHistory.getUsedPoint());
    }

    public long getOrderId() {
        return orderId;
    }

    public int getEarnedPoint() {
        return earnedPoint;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

}

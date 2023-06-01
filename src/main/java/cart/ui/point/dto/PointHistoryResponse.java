package cart.ui.point.dto;

import cart.application.service.point.PointHistoryDto;

public class PointHistoryResponse {

    private final long orderId;
    private final int earnedPoint;
    private final int usedPoint;

    private PointHistoryResponse(final long orderId, final int earnedPoint, final int usedPoint) {
        this.orderId = orderId;
        this.earnedPoint = earnedPoint;
        this.usedPoint = usedPoint;
    }

    public static PointHistoryResponse from(final PointHistoryDto pointHistoryDto) {
        return new PointHistoryResponse(pointHistoryDto.getOrderId(), pointHistoryDto.getEarnedPoint(), pointHistoryDto.getUsedPoint());
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

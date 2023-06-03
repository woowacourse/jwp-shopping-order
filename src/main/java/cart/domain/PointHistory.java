package cart.domain;

public class PointHistory {

    private final long orderId;
    private final int earnedPoint;
    private final int usedPoint;

    public PointHistory(Long orderId, int earnedPoint, int usedPoint) {
        this.orderId = orderId;
        this.earnedPoint = earnedPoint;
        this.usedPoint = usedPoint;
    }

    public int calculatePoint() {
        return earnedPoint - usedPoint;
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

package cart.domain;

public class PointHistory {

    private final long orderId;
    private final int usedPoint;
    private final int earnedPoint;

    public PointHistory(final long orderId, final int usedPoint, final int earnedPoint) {
        this.orderId = orderId;
        this.usedPoint = usedPoint;
        this.earnedPoint = earnedPoint;
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

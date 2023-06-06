package cart.dto.response;

public class OrderAdditionResponse {

    private long orderId;
    private long newEarnedPoint;

    public OrderAdditionResponse(final long orderId, final long newEarnedPoint) {
        this.orderId = orderId;
        this.newEarnedPoint = newEarnedPoint;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getNewEarnedPoint() {
        return newEarnedPoint;
    }
}

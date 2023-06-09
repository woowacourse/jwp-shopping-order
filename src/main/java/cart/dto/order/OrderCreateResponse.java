package cart.dto.order;

public class OrderCreateResponse {

    private Long orderId;
    private Long newEarnedPoint;

    public OrderCreateResponse() {
    }

    public OrderCreateResponse(Long orderId, Long newEarnedPoint) {
        this.orderId = orderId;
        this.newEarnedPoint = newEarnedPoint;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getNewEarnedPoint() {
        return newEarnedPoint;
    }
}

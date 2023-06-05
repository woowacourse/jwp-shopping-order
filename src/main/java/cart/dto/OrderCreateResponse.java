package cart.dto;

public class OrderCreateResponse {

    private final long orderId;

    public OrderCreateResponse(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }
}

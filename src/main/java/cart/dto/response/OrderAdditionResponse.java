package cart.dto.response;

public class OrderAdditionResponse {

    private long orderId;

    public OrderAdditionResponse(final long orderId) {
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }
}

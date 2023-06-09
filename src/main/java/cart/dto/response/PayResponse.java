package cart.dto.response;

public class PayResponse {
    private final Long orderId;

    public PayResponse(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}

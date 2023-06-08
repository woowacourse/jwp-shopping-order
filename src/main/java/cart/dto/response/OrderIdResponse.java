package cart.dto.response;

public class OrderIdResponse {

    private Long orderId;

    public OrderIdResponse(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}

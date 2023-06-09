package cart.dto.response;

public class OrderIdResponse {

    private Long orderId;

    public OrderIdResponse() {
    }

    public OrderIdResponse(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}

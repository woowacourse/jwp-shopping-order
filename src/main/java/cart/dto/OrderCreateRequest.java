package cart.dto;

import java.util.List;

public class OrderCreateRequest {

    private List<OrderItemRequest> orderItemRequests;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(List<OrderItemRequest> orderItemRequests) {
        this.orderItemRequests = orderItemRequests;
    }

    public List<OrderItemRequest> getOrderItemRequests() {
        return orderItemRequests;
    }
}

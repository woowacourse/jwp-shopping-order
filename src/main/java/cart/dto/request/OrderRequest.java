package cart.dto.request;

import java.util.List;

public class OrderRequest {

    private List<OrderItemRequest> orderItemRequests;

    public OrderRequest() {
    }

    public OrderRequest(List<OrderItemRequest> orderItemRequests) {
        this.orderItemRequests = orderItemRequests;
    }

    public List<OrderItemRequest> getOrderItemRequests() {
        return orderItemRequests;
    }
}

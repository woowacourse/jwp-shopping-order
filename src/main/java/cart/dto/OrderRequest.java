package cart.dto;

import java.util.List;

public class OrderRequest {
    private List<OrderItemRequest> orderItems;

    public OrderRequest() {
    }

    public OrderRequest(List<OrderItemRequest> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }
}

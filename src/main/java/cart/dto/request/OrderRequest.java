package cart.dto.request;

import java.util.List;

public class OrderRequest {
    private List<OrderItemRequest> orderItems;
    private String orderTime;

    public OrderRequest() {
    }

    public OrderRequest(List<OrderItemRequest> orderItems, String orderTime) {
        this.orderItems = orderItems;
        this.orderTime = orderTime;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public String getOrderTime() {
        return orderTime;
    }
}

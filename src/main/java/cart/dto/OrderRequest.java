package cart.dto;

import java.sql.Timestamp;
import java.util.List;

public class OrderRequest {
    private List<OrderItemRequest> orderItems;
    private Timestamp orderTime;

    public OrderRequest() {
    }

    public OrderRequest(List<OrderItemRequest> orderItems, Timestamp orderTime) {
        this.orderItems = orderItems;
        this.orderTime = orderTime;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }
}

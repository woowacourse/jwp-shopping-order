package cart.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderAddRequest {
    
    private LocalDateTime createdAt;
    private List<OrderItemRequest> orderItemRequestList;
    
    public OrderAddRequest(LocalDateTime createdAt, List<OrderItemRequest> orderItemRequestList) {
        this.createdAt = createdAt;
        this.orderItemRequestList = orderItemRequestList;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public List<OrderItemRequest> getOrderItemRequestList() {
        return orderItemRequestList;
    }
}

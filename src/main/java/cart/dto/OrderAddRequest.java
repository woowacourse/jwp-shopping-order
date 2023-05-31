package cart.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderAddRequest {
    
    private LocalDateTime orderTime;
    private List<OrderItemRequest> orderItems;
    
    public OrderAddRequest(LocalDateTime orderTime, List<OrderItemRequest> orderItems) {
        this.orderTime = orderTime;
        this.orderItems = orderItems;
    }
    
    public LocalDateTime getOrderTime() {
        return orderTime;
    }
    
    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }
}

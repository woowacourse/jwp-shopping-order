package cart.dto;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderAddRequest {
    @NotNull
    private LocalDateTime orderTime;
    @NotEmpty(message = "주문할 상품이 존재하지 않습니다.")
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

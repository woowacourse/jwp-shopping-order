package cart.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private final Long orderId;
    private final List<OrderProductResponse> orderProducts;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    public OrderResponse(Long orderId, List<OrderProductResponse> orderProducts, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderProducts = orderProducts;
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderProductResponse> getOrderProducts() {
        return orderProducts;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

package cart.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private long orderId;
    private List<OrderProductResponse> orderProducts;
    private int orderTotalPrice;
    private int usedPoint;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public OrderResponse() {
    }

    public OrderResponse(long orderId, List<OrderProductResponse> orderProducts, int orderTotalPrice, int usedPoint, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderProducts = orderProducts;
        this.orderTotalPrice = orderTotalPrice;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
    }

    public long getOrderId() {
        return orderId;
    }

    public List<OrderProductResponse> getOrderProducts() {
        return orderProducts;
    }

    public int getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

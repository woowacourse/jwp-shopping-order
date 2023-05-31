package cart.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailResponse {

    private Long orderId;
    private LocalDateTime createdAt;
    private List<OrderProductResponse> orderItems;
    private int totalPrice;
    private int usedPoint;
    private int earnedPoint;

    public OrderDetailResponse(Long orderId, LocalDateTime createdAt, List<OrderProductResponse> orderItems, int totalPrice, int usedPoint, int earnedPoint) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.earnedPoint = earnedPoint;
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderProductResponse> getOrderItems() {
        return orderItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getEarnedPoint() {
        return earnedPoint;
    }
}

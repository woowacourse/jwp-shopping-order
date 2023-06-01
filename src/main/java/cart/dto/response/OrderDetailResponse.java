package cart.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailResponse {
    private Long orderId;
    private Long totalPrice;
    private Long spendPoint;
    private Long spendPrice;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> orderItemResponses;

    private OrderDetailResponse() {
    }

    public OrderDetailResponse(Long orderId, Long totalPrice, Long spendPoint, Long spendPrice, LocalDateTime createdAt,
                               List<OrderItemResponse> orderItemResponses) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.spendPoint = spendPoint;
        this.spendPrice = spendPrice;
        this.createdAt = createdAt;
        this.orderItemResponses = orderItemResponses;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public Long getSpendPoint() {
        return spendPoint;
    }

    public Long getSpendPrice() {
        return spendPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItemResponse> getOrderItemResponses() {
        return orderItemResponses;
    }
}

package cart.dto;

import java.time.LocalDateTime;

public class OrderResponse {
    private Long orderId;
    private String thumbnail;
    private Long spendPrice;
    private LocalDateTime createdAt;

    private OrderResponse() {
    }

    public OrderResponse(Long orderId, String thumbnail, Long spendPrice, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.thumbnail = thumbnail;
        this.spendPrice = spendPrice;
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Long getSpendPrice() {
        return spendPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

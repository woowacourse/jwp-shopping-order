package cart.dto.response;

import java.time.LocalDateTime;

public class OrderResponse {
    private Long orderId;
    private String thumbnail;
    private String firstProductName;
    private Integer totalCount;
    private Long spendPrice;
    private LocalDateTime createdAt;

    private OrderResponse() {
    }

    public OrderResponse(Long orderId, String thumbnail, String firstProductName,
                         Integer totalCount,
                         Long spendPrice, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.thumbnail = thumbnail;
        this.firstProductName = firstProductName;
        this.totalCount = totalCount;
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

    public String getFirstProductName() {
        return firstProductName;
    }

    public Integer getTotalCount() {
        return totalCount;
    }
}

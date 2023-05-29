package cart.application.dto;

import java.time.LocalDateTime;

public class GetOrderResponse {

    private final Long orderId;
    private final Integer payAmount;
    private final LocalDateTime orderAt;
    private final String productName;
    private final String productImageUrl;
    private final Integer totalProductCount;

    public GetOrderResponse(Long orderId, Integer payAmount, LocalDateTime orderAt, String productName,
        String productImageUrl,
        Integer totalProductCount) {
        this.orderId = orderId;
        this.payAmount = payAmount;
        this.orderAt = orderAt;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.totalProductCount = totalProductCount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Integer getPayAmount() {
        return payAmount;
    }

    public LocalDateTime getOrderAt() {
        return orderAt;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public Integer getTotalProductCount() {
        return totalProductCount;
    }
}

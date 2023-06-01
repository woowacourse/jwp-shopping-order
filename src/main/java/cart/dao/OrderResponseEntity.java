package cart.dao;

import java.time.LocalDateTime;

public class OrderResponseEntity {
    private Long orderId;
    private LocalDateTime orderedAt;
    private Long orderItemId;
    private Integer quantity;
    private Long productId;
    private String productName;
    private Integer productPrice;
    private String productImageUrl;

    public OrderResponseEntity(Long orderId, LocalDateTime orderedAt, Long orderItemId, Integer quantity, Long productId, String productName, Integer productPrice, String productImageUrl) {
        this.orderId = orderId;
        this.orderedAt = orderedAt;
        this.orderItemId = orderItemId;
        this.quantity = quantity;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }
}

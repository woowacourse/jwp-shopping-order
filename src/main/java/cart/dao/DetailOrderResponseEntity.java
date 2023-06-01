package cart.dao;

import java.time.LocalDateTime;

public class DetailOrderResponseEntity {
    private Long orderId;
    private LocalDateTime orderedAt;
    private Long usedPoint;
    private Long orderItemId;
    private Long quantity;
    private Long productId;
    private String productName;
    private Long productPrice;
    private String productImageUrl;

    public DetailOrderResponseEntity(Long orderId, LocalDateTime orderedAt, Long usedPoint, Long orderItemId, Long quantity, Long productId, String productName, Long productPrice, String productImageUrl) {
        this.orderId = orderId;
        this.orderedAt = orderedAt;
        this.usedPoint = usedPoint;
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

    public Long getUsedPoint() {
        return usedPoint;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }
}

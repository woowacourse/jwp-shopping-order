package cart.dao.entity;

import java.time.LocalDateTime;

public class OrderProductEntity {

    private Long id;
    private Long orderId;
    private Long productId;
    private String orderProductName;
    private Integer orderProductPrice;
    private String orderProductImageUrl;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderProductEntity(Long id, Long orderId, Long productId, String orderProductName, Integer orderProductPrice,
            String orderProductImageUrl, Integer quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.orderProductName = orderProductName;
        this.orderProductPrice = orderProductPrice;
        this.orderProductImageUrl = orderProductImageUrl;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getOrderProductName() {
        return orderProductName;
    }

    public Integer getOrderProductPrice() {
        return orderProductPrice;
    }

    public String getOrderProductImageUrl() {
        return orderProductImageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

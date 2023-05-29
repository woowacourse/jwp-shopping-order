package cart.dao.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderProductEntity {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final String orderProductName;
    private final Integer orderProductPrice;
    private final String orderProductImageUrl;
    private final Integer quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

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

    public OrderProductEntity assignId(Long id) {
        return new OrderProductEntity(
                id,
                orderId,
                productId,
                orderProductName,
                orderProductPrice,
                orderProductImageUrl,
                quantity,
                createdAt,
                updatedAt
        );
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderProductEntity that = (OrderProductEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

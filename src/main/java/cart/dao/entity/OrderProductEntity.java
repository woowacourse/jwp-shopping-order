package cart.dao.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderProductEntity {

    private final Long id;
    private final Long orderId;
    private final ProductEntity productEntity;
    private final String productName;
    private final Integer productPrice;
    private final String productImageUrl;
    private final Integer quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public OrderProductEntity(
            Long orderId,
            ProductEntity productEntity,
            String productName,
            Integer productPrice,
            String productImageUrl,
            Integer quantity
    ) {
        this(null, orderId, productEntity, productName, productPrice, productImageUrl, quantity, null, null);
    }

    public OrderProductEntity(
            Long id,
            Long orderId,
            ProductEntity productEntity,
            String productName,
            Integer productPrice,
            String productImageUrl,
            Integer quantity
    ) {
        this(id, orderId, productEntity, productName, productPrice, productImageUrl, quantity, null, null);
    }

    public OrderProductEntity(
            Long id,
            Long orderId,
            ProductEntity productEntity,
            String productName,
            Integer productPrice,
            String productImageUrl,
            Integer quantity,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.orderId = orderId;
        this.productEntity = productEntity;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public OrderProductEntity assignId(Long id) {
        return new OrderProductEntity(
                id,
                orderId,
                productEntity,
                productName,
                productPrice,
                productImageUrl,
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

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public Long getProductId() {
        return productEntity != null ? productEntity.getId() : null;
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package cart.dao.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderProductEntity {
    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final String productName;
    private final int productPrice;
    private final String productImageUrl;
    private final int quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public OrderProductEntity(final Long id, final Long orderId, final ProductEntity productEntity, final int quantity,
                              final LocalDateTime createdAt, final LocalDateTime updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productEntity.getId();
        this.productName = productEntity.getName();
        this.productImageUrl = productEntity.getImageUrl();
        this.productPrice = productEntity.getPrice();
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public OrderProductEntity(final Long id, final Long orderId, final ProductEntity productEntity,
                              final int quantity) {
        this(id, orderId, productEntity, quantity, null, null);
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

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
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

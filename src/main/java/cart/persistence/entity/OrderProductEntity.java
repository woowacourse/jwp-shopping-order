package cart.persistence.entity;

import java.time.LocalDateTime;

public class OrderProductEntity {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final Integer purchasedPrice;
    private final Integer quantity;
    private final LocalDateTime createdAt;

    public OrderProductEntity(final Long id, final Long orderId, final Long productId, final Integer purchasedPrice, final Integer quantity, final LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.purchasedPrice = purchasedPrice;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public OrderProductEntity(final Long id, final Long orderId, final Long productId, final Integer purchasedPrice, final Integer quantity) {
        this(id, orderId, productId, purchasedPrice, quantity, null);
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

    public Integer getPurchasedPrice() {
        return purchasedPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

package cart.persistence.entity;

import java.time.LocalDateTime;

public class CartItemEntity {

    private final Long id;
    private final Long memberId;
    private final Long productId;
    private final Integer quantity;
    private final LocalDateTime createdAt;

    public CartItemEntity(final Long id, final Long memberId, final Long productId, final Integer quantity, final LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

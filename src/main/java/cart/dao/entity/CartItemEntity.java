package cart.dao.entity;

import java.time.LocalDateTime;

public class CartItemEntity {
    private final Long id;
    private final Long memberId;
    private final Long productId;
    private final int quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CartItemEntity(final Long id, final Long memberId, final Long productId,
                          final int quantity,
                          final LocalDateTime createdAt, final LocalDateTime updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

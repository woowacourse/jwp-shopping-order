package cart.dao.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class CartProductItemEntity {

    private final Long id;
    private final Long memberId;
    private final ProductEntity productEntity;
    private final Integer quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CartProductItemEntity(Long id, Long memberId, ProductEntity productEntity, Integer quantity, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.productEntity = productEntity;
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

    public ProductEntity getProductEntity() {
        return productEntity;
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
        CartProductItemEntity that = (CartProductItemEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

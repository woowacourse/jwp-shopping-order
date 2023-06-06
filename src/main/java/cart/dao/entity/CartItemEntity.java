package cart.dao.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class CartItemEntity {

    private final Long id;
    private final MemberEntity memberEntity;
    private final ProductEntity productEntity;
    private final Integer quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CartItemEntity(MemberEntity memberEntity, ProductEntity productEntity, Integer quantity) {
        this(null, memberEntity, productEntity, quantity, null, null);
    }

    public CartItemEntity(Long id, MemberEntity memberEntity, ProductEntity productEntity, Integer quantity) {
        this(id, memberEntity, productEntity, quantity, null, null);
    }

    public CartItemEntity(
            Long id,
            MemberEntity memberEntity,
            ProductEntity productEntity,
            Integer quantity,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.memberEntity = memberEntity;
        this.productEntity = productEntity;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CartItemEntity assignId(Long id) {
        return new CartItemEntity(id, memberEntity, productEntity, quantity, createdAt, updatedAt);
    }

    public Long getId() {
        return id;
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    public Long getMemberId() {
        return memberEntity.getId();
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public Long getProductId() {
        return productEntity.getId();
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
        CartItemEntity that = (CartItemEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

package cart.entity;

import cart.domain.CartItem;

public class CartItemEntity {

    private final Long id;
    private final ProductEntity productEntity;
    private final MemberInfoEntity memberEntity;
    private final int quantity;

    public CartItemEntity(ProductEntity productEntity, MemberInfoEntity memberEntity) {
        this(null, productEntity, memberEntity, 1);
    }

    public CartItemEntity(Long id, ProductEntity productEntity, MemberInfoEntity memberEntity, int quantity) {
        this.id = id;
        this.productEntity = productEntity;
        this.memberEntity = memberEntity;
        this.quantity = quantity;
    }

    public CartItem toDomain() {
        return new CartItem(id, quantity, productEntity.toDomain(), memberEntity.toDomain());
    }

    public Long getId() {
        return id;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public MemberInfoEntity getMemberEntity() {
        return memberEntity;
    }

    public int getQuantity() {
        return quantity;
    }
}

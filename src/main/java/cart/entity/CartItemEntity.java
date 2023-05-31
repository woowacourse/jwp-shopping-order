package cart.entity;

import cart.domain.cart.CartItem;

public class CartItemEntity {

    private final Long id;
    private final int quantity;
    private final ProductEntity productEntity;
    private final MemberEntity memberEntity;

    public CartItemEntity(
            final int quantity,
            final ProductEntity productEntity,
            final MemberEntity memberEntity
    ) {
        this(null, quantity, productEntity, memberEntity);
    }

    public CartItemEntity(
            final Long id,
            final int quantity,
            final ProductEntity productEntity,
            final MemberEntity memberEntity
    ) {
        this.id = id;
        this.quantity = quantity;
        this.productEntity = productEntity;
        this.memberEntity = memberEntity;
    }

    public static CartItemEntity from(final CartItem cartItem) {
        return new CartItemEntity(
                cartItem.getId(),
                cartItem.getQuantity().quantity(),
                ProductEntity.from(cartItem.getProduct()),
                MemberEntity.from(cartItem.getMember())

        );
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }
}

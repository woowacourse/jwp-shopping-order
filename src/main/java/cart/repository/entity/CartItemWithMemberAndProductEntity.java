package cart.repository.entity;

public class CartItemWithMemberAndProductEntity {
    private final CartItemEntity cartItemEntity;
    private final MemberEntity memberEntity;
    private final ProductEntity productEntity;

    public CartItemWithMemberAndProductEntity(final CartItemEntity cartItemEntity, final MemberEntity memberEntity,
                                              final ProductEntity productEntity) {
        this.cartItemEntity = cartItemEntity;
        this.memberEntity = memberEntity;
        this.productEntity = productEntity;
    }

    public CartItemEntity getCartItemEntity() {
        return cartItemEntity;
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }
}

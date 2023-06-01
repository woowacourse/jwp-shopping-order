package cart.persistence.dto;

import cart.domain.Member;
import cart.domain.Product;
import cart.domain.cartitem.CartItem;
import cart.persistence.entity.CartItemEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.ProductEntity;

public class CartDetailDTO {

    private final CartItemEntity cartItemEntity;
    private final MemberEntity memberEntity;
    private final ProductEntity productEntity;

    public CartDetailDTO(final CartItemEntity cartItemEntity, final MemberEntity memberEntity,
            final ProductEntity productEntity) {
        this.cartItemEntity = cartItemEntity;
        this.memberEntity = memberEntity;
        this.productEntity = productEntity;
    }

    public CartItem toDomain() {
        Product product = productEntity.toDomain();
        Member member = memberEntity.toDomain();
        return new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), product, member);
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

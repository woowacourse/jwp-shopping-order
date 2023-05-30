package cart.application.mapper;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.persistence.dto.CartDetailDTO;
import cart.persistence.entity.CartItemEntity;

public class CartItemMapper {

    private CartItemMapper() {
    }

    public static CartItemEntity toEntity(final CartItem cartItem) {
        return new CartItemEntity(cartItem.getId(), cartItem.getMember().getId(), cartItem.getProduct().getId(),
                cartItem.getQuantity());
    }

    public static CartItem toCartItem(final CartDetailDTO cartDetail) {
        CartItemEntity cartItemEntity = cartDetail.getCartItemEntity();
        Product product = ProductMapper.toProduct(cartDetail.getProductEntity());
        Member member = MemberMapper.toMember(cartDetail.getMemberEntity());
        return new CartItem(cartItemEntity.getId(), cartItemEntity.getQuantity(), product, member);
    }
}

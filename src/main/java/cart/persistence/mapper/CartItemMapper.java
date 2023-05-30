package cart.persistence.mapper;

import static cart.persistence.mapper.MemberMapper.convertMember;
import static cart.persistence.mapper.ProductMapper.convertProduct;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.dto.CartItemWithId;
import cart.domain.member.dto.MemberWithId;
import cart.domain.product.dto.ProductWithId;
import cart.persistence.dao.dto.CartItemDto;

public class CartItemMapper {

    public static CartItem convertCartItem(final CartItemDto cartItemDto) {
        return new CartItem(cartItemDto.getProductQuantity(), convertProduct(cartItemDto), convertMember(cartItemDto));
    }

    public static CartItemWithId convertCartItemWithId(final CartItemDto cartItemDto) {
        final ProductWithId productWithId = new ProductWithId(cartItemDto.getProductId(), convertProduct(cartItemDto));
        final MemberWithId memberWithId = new MemberWithId(cartItemDto.getMemberId(), convertMember(cartItemDto));
        return new CartItemWithId(cartItemDto.getCartId(), cartItemDto.getProductQuantity(), productWithId,
            memberWithId);
    }
}

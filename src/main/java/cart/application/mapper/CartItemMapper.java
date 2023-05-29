package cart.application.mapper;

import cart.domain.CartItem;
import cart.persistence.entity.CartItemEntity;

public class CartItemMapper {

    public static CartItemEntity toEntity(final CartItem cartItem) {
        return new CartItemEntity(cartItem.getId(), cartItem.getMember().getId(), cartItem.getProduct().getId(),
                cartItem.getQuantity());
    }
}

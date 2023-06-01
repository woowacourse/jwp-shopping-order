package cart.mapper;

import cart.domain.CartItem;
import cart.dto.CartItemResponse;

public class CartItemMapper {
    private CartItemMapper() {
    }

    public static CartItemResponse toResponse(CartItem cartItem) {
        return new CartItemResponse(cartItem.getId(), cartItem.getQuantity(), ProductMapper.toResponse(cartItem.getProduct()));
    }
}

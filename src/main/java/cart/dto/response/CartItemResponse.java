package cart.dto.response;

import cart.domain.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CartItemResponse {

    private final Long cartItemId;
    private final int quantity;
    private final ProductResponse product;

    public static CartItemResponse from(final CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantityValue(),
                ProductResponse.from(cartItem.getProduct())
        );
    }
}

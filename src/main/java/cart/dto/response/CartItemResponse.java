package cart.dto.response;

import cart.domain.CartItem;

public class CartItemResponse {

    private final Long cartItemId;
    private final int quantity;
    private final ProductResponse product;

    public CartItemResponse(Long cartItemId, int quantity, ProductResponse product) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse from(final CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantityValue(),
                ProductResponse.from(cartItem.getProduct())
        );
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}

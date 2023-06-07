package cart.dto.cart;

import cart.domain.cart.CartItem;
import cart.dto.product.ProductResponse;

public class CartItemResponse {

    private Long cartItemId;
    private int quantity;
    private ProductResponse product;

    private CartItemResponse() {

    }

    private CartItemResponse(final Long CartItemId, final int quantity, final ProductResponse product) {
        this.cartItemId = CartItemId;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse from(final CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
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

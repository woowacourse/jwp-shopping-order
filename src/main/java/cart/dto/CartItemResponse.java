package cart.dto;

import cart.domain.CartItem;

public class CartItemResponse {
    private Long cartItemId;
    private int quantity;
    private ProductResponse product;

    private CartItemResponse(Long cartItemId, int quantity, ProductResponse product) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse of(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductResponse.of(cartItem.getProduct())
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

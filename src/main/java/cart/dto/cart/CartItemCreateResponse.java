package cart.dto.cart;

public class CartItemCreateResponse {
    private Long cartItemId;

    public CartItemCreateResponse() {
    }

    public CartItemCreateResponse(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getCartItemId() {
        return cartItemId;
    }
}

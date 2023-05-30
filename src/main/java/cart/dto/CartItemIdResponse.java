package cart.dto;

public class CartItemIdResponse {

    private final Long cartItemId;

    public CartItemIdResponse(final Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getCartItemId() {
        return cartItemId;
    }
}

package cart.dto.response;

public class CartItemAdditionResponse {

    private long cartItemId;

    public CartItemAdditionResponse(final long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public long getCartItemId() {
        return cartItemId;
    }
}

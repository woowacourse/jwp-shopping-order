package cart.dto.request;

public class CartItemIdRequest {
    private Long cartItemId;

    public CartItemIdRequest() {
    }

    public CartItemIdRequest(final Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public static CartItemIdRequest of(final Long cartItem1Id) {
        return new CartItemIdRequest(cartItem1Id);
    }

    public Long getCartItemId() {
        return cartItemId;
    }
}

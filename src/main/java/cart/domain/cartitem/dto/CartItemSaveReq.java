package cart.domain.cartitem.dto;

public class CartItemSaveReq {

    private final Long cartItemId;
    private final int cartItemQuantity;

    public CartItemSaveReq(final Long cartItemId, final int cartItemQuantity) {
        this.cartItemId = cartItemId;
        this.cartItemQuantity = cartItemQuantity;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public int getCartItemQuantity() {
        return cartItemQuantity;
    }
}

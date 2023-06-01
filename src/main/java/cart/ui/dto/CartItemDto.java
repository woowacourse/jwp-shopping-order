package cart.ui.dto;

public class CartItemDto {

    private Long cartItemId;

    public CartItemDto() {
    }

    public CartItemDto(final Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getCartItemId() {
        return cartItemId;
    }
}

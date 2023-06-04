package cart.dto.cart;

public class CartItemQuantityUpdateRequest {
    private Long quantity;

    public CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(final Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }
}

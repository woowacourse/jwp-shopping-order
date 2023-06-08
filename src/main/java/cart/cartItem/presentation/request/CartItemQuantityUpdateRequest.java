package cart.cartItem.presentation.request;

import cart.cartItem.application.dto.CartItemQuantityUpdateDto;

public class CartItemQuantityUpdateRequest {
    private int quantity;

    public CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    public CartItemQuantityUpdateDto toDto() {
        return new CartItemQuantityUpdateDto(quantity);
    }
    public int getQuantity() {
        return quantity;
    }
}

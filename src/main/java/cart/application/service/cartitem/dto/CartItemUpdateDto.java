package cart.application.service.cartitem.dto;

import cart.ui.cartitem.dto.CartItemQuantityUpdateRequest;

public class CartItemUpdateDto {

    private final int quantity;

    public CartItemUpdateDto(int quantity) {
        this.quantity = quantity;
    }

    public static CartItemUpdateDto from(final CartItemQuantityUpdateRequest cartItemQuantityUpdateRequest) {
        return new CartItemUpdateDto(cartItemQuantityUpdateRequest.getQuantity());
    }

    public int getQuantity() {
        return quantity;
    }

}

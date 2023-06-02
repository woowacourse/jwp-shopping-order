package cart.dto;

import javax.validation.constraints.PositiveOrZero;

public class CartItemQuantityUpdateRequest {

    @PositiveOrZero(message = "수량은 0 이상이어야 합니다.")
    private int quantity;

    private CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(final int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}

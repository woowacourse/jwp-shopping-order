package cart.dto;

import javax.validation.constraints.PositiveOrZero;

public class CartItemQuantityUpdateRequest {

    @PositiveOrZero
    private int quantity;

    public CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}

package cart.dto;

import javax.validation.constraints.NotNull;

public class CartItemQuantityUpdateRequest {
    @NotNull
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

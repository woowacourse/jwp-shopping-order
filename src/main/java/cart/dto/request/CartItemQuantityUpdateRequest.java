package cart.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CartItemQuantityUpdateRequest {
    @Positive
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

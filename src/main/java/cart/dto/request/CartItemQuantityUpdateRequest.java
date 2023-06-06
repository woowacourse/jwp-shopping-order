package cart.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class CartItemQuantityUpdateRequest {
    @NotEmpty
    @Positive
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

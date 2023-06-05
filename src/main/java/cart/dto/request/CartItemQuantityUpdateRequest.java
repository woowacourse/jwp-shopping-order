package cart.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class CartItemQuantityUpdateRequest {
    @NotBlank
    @Min(0)
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

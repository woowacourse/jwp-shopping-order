package cart.ui.dto.cartitem;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class CartItemQuantityUpdateRequest {

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    public CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}

package cart.dto;

import javax.validation.constraints.NotNull;

public class CartItemQuantityUpdateRequest {
    @NotNull(message = "quantity는 null이 불가능합니다")
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

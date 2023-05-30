package cart.dto;

import javax.validation.constraints.PositiveOrZero;

public class CartItemQuantityUpdateRequest {

    @PositiveOrZero(message = "수량은 음수가 될 수 없습니다. 입력값: ${validatedValue}")
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

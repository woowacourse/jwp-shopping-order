package cart.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

public class CartItemQuantityUpdateRequest {

    @Positive(message = "수량은 음수가 될 수 없습니다.")
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

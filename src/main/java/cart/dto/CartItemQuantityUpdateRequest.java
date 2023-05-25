package cart.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CartItemQuantityUpdateRequest {

    @NotNull(message = "수량을 입력해야 합니다.")
    @Min(value = 0, message = "수량은 음수가 될 수 없습니다.")
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

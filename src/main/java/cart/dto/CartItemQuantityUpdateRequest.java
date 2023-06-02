package cart.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CartItemQuantityUpdateRequest {
    @NotNull
    @Min(value = 0, message = "{value} 이상의 값을 입력해주세요")
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

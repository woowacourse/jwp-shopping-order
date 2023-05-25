package cart.dto;

import javax.validation.constraints.NotNull;

public class CartItemQuantityUpdateRequest {

    @NotNull(message = "수량을 입력해 주세요. 입력값 : ${validatedValue}")
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

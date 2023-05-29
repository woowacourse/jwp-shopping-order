package cart.dto;

import javax.validation.constraints.PositiveOrZero;

public class CartItemQuantityUpdateRequest {

    @PositiveOrZero(message = "수정할 수량은 0 이상인 정수여야합니다.")
    private int quantity;

    private CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}

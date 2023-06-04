package cart.application.request;

import javax.validation.constraints.PositiveOrZero;

public class UpdateCartItemQuantityRequest {

    @PositiveOrZero(message = "수량은 음수가 될 수 없습니다. 입력값: ${validatedValue}")
    private Integer quantity;

    public UpdateCartItemQuantityRequest() {
    }

    public UpdateCartItemQuantityRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}

package cart.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class CartItemQuantityUpdateRequest {

    @NotNull(message = "수량을 입력해 주세요. 입력값 : ${validatedValue}")
    @PositiveOrZero(message = "카트 아이템의 수량은 0 이상으로 입력해 주세요. 입력값 : ${validatedValue}")
    private Integer quantity;

    private CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}

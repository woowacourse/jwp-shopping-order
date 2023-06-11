package cart.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class CartItemQuantityUpdateRequest {
    @NotNull(message = "상품 수량은 필수 입력 값입니다.")
    @PositiveOrZero(message = "상품 수량은 0 이상이어야 합니다.")
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

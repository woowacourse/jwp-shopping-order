package cart.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class CartItemQuantityUpdateRequest {

    @NotNull(message = "quantity 필드가 필요합니다.")
    @PositiveOrZero(message = "상품 수량이 음수이면 안됩니다.")
    private Integer quantity;

    public CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}

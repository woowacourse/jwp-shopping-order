package cart.dto;

import javax.validation.constraints.PositiveOrZero;

public class CartItemQuantityUpdateRequest {
    @PositiveOrZero(message = "수량은 0이상이어야 합니다.")
    private Integer quantity;

    public CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(final Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }
}

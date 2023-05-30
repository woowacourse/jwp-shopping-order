package cart.application.dto.cartitem;

import javax.validation.constraints.NotNull;

public class CartItemQuantityUpdateRequest {

    @NotNull(message = "수량 정보는 비어있을 수 없습니다.")
    private final Integer quantity;

    public CartItemQuantityUpdateRequest() {
        this(null);
    }

    public CartItemQuantityUpdateRequest(Integer quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}

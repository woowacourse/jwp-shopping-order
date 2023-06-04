package cart.dto.cartitem;

import javax.validation.constraints.PositiveOrZero;

public class CartItemQuantityUpdateRequest {
    @PositiveOrZero(message = "장바구니 상품의 수량은 0 이상의 값이어야 합니다.")
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

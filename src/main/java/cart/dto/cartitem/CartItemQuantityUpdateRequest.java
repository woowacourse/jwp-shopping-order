package cart.dto.cartitem;

import javax.validation.constraints.Positive;

public class CartItemQuantityUpdateRequest {
    @Positive(message = "변경할 장바구니 상품의 수량은 1이상이어야 합니다.")
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

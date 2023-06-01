package cart.dto.cartItem;

import javax.validation.constraints.Min;

public class CartItemQuantityUpdateRequest {

    @Min(value = 0, message = "상품 개수는 음수일 수 없습니다.")
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

package cart.dto;

import javax.validation.constraints.Positive;

public class CartItemQuantityUpdateRequest {
    @Positive(message = "상품 수량은 양수여야 합니다.")
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

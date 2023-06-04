package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CartItemQuantityUpdateRequest {
    @Schema(description = "장바구니에 추가된 상품의 수량")
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

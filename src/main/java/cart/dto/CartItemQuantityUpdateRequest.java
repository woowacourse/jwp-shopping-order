package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장바구니 수량 변경 요청")
public class CartItemQuantityUpdateRequest {

    @Schema(description = "변경할 수량", example = "1")
    private int quantity;

    public CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(final int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}

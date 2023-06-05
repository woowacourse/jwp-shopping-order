package cart.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.PositiveOrZero;

@Schema(description = "장바구니 수량 변경 Request")
public class CartItemQuantityUpdateRequest {

    @Schema(description = "장바구니 상품 수량", example = "2")
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

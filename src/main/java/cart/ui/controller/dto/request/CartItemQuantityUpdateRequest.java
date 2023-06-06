package cart.ui.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

@Schema(description = "장바구니 상품 수량 업데이트 요청")
public class CartItemQuantityUpdateRequest {

    @Schema(description = "장바구니 업데이트 수량", example = "5")
    @NotNull(message = "장바구니 상품 수량은 필수로 입력해야 합니다.")
    private Integer quantity;

    private CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }
}

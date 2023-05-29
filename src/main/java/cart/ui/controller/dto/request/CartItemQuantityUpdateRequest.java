package cart.ui.controller.dto.request;

import javax.validation.constraints.NotNull;

public class CartItemQuantityUpdateRequest {

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

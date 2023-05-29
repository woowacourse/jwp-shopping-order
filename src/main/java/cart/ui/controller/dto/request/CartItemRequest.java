package cart.ui.controller.dto.request;

import javax.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull(message = "장바구니 등록을 위해 상품 ID는 필수입니다.")
    private Long productId;

    private CartItemRequest() {
    }

    public CartItemRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}

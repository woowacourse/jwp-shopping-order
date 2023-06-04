package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CartItemRequest {
    @Schema(description = "장바구니에 추가된 상품의 ID")
    private Long productId;

    public CartItemRequest() {
    }

    public CartItemRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}

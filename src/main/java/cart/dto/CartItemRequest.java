package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장바구니 아이템 추가 요청")
public class CartItemRequest {

    @Schema(description = "추가하고자 하는 상품 ID", example = "1")
    private Long productId;

    public CartItemRequest() {
    }

    public CartItemRequest(final Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}

package cart.dto;

import javax.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull(message = "상품의 ID를 입력하세요")
    private Long productId;

    private CartItemRequest() {
    }

    private CartItemRequest(final Long productId) {
        this.productId = productId;
    }

    public static CartItemRequest from(final Long productId) {
        return new CartItemRequest(productId);
    }

    public Long getProductId() {
        return productId;
    }
}

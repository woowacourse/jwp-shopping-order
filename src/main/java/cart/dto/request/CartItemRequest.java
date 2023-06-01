package cart.dto.request;

import javax.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull(message = "상품 ID를 입력해 주세요.")
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

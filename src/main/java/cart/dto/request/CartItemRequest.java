package cart.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CartItemRequest {
    @NotNull(message = "상품 ID는 반드시 포함되어야 합니다.")
    @Positive(message = "상품 ID는 0 또는 음수가 될 수 없습니다.")
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

package cart.dto.request;

import javax.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull(message = "productId 필드가 필요합니다.")
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

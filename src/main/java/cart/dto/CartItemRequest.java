package cart.dto;

import javax.validation.constraints.NotNull;

public class CartItemRequest {
    @NotNull(message = "productId는 null이 불가능합니다")
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

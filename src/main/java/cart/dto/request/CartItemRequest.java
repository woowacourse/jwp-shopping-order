package cart.dto.request;

import javax.validation.constraints.NotBlank;

public class CartItemRequest {
    @NotBlank
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

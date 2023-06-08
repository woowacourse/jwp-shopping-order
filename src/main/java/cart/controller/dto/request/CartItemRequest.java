package cart.controller.dto.request;

import javax.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull(message = "productId 가 null 입니다.")
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

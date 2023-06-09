package cart.dto.cartItem;

import javax.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull
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

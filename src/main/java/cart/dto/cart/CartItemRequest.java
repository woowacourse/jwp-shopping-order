package cart.dto.cart;

import javax.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull
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

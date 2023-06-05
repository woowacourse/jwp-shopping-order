package cart.ui.dto.cartitem;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CartItemRequest {

    @NotNull
    @Positive
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

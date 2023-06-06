package cart.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class CartItemAddRequest {
    @NotEmpty
    @Positive
    private Long productId;

    public CartItemAddRequest() {
    }

    public CartItemAddRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}

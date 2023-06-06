package cart.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class CartItemAddRequest {

    private final Long productId;

    @JsonCreator
    public CartItemAddRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}

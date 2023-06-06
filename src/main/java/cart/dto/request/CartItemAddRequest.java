package cart.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import javax.validation.constraints.NotNull;

public class CartItemAddRequest {
    @NotNull
    private final Long productId;

    @JsonCreator
    public CartItemAddRequest(final Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}

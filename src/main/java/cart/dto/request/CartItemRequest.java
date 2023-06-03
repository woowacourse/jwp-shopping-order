package cart.dto.request;

import java.beans.ConstructorProperties;
import javax.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull
    private Long productId;

    @ConstructorProperties(value = {"productId"})
    public CartItemRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}

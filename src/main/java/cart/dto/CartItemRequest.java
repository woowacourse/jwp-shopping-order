package cart.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CartItemRequest {
    @NotNull
    @Min(value = 0, message = "{value} 이상의 값을 입력해주세요")
    private long productId;

    public CartItemRequest() {
    }

    public CartItemRequest(long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}

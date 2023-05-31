package cart.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class CartItemQuantityUpdateRequest {

    @Min(value = 0, message = "잘못된 요청입니다")
    @Max(value = 99, message = "잘못된 요청입니다")
    private Long quantity;

    public CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }
}

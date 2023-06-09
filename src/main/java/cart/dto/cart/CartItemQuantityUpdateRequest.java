package cart.dto.cart;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CartItemQuantityUpdateRequest {

    @NotNull
    @Min(value = 0, message = "잘못된 요청입니다")
    @Max(value = 0, message = "잘못된 요청입니다")
    private Long quantity;

    public CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(final Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }
}

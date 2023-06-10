package cart.presentation.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class CartItemQuantityRequest {

    @PositiveOrZero
    @NotNull
    private Long quantity;

    public CartItemQuantityRequest() {
    }

    public CartItemQuantityRequest(Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }
}

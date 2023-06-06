package cart.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import javax.validation.constraints.NotNull;

public class CartItemQuantityUpdateRequest {
    @NotNull
    private final Integer quantity;

    @JsonCreator
    public CartItemQuantityUpdateRequest(final Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }
}

package cart.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class CartItemQuantityUpdateRequest {
    private final Integer quantity;

    @JsonCreator
    public CartItemQuantityUpdateRequest(final Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }
}

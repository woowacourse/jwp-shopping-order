package cart.dto.request;

import java.beans.ConstructorProperties;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CartItemQuantityUpdateRequest {

    @NotNull
    @Min(1)
    private final int quantity;

    @ConstructorProperties(value = {"quantity"})
    public CartItemQuantityUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}

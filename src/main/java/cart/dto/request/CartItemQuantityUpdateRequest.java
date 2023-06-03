package cart.dto.request;

import java.beans.ConstructorProperties;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public class CartItemQuantityUpdateRequest {

    @NotNull
    @Range(min = 1)
    private int quantity;

    @ConstructorProperties(value = {"quantity"})
    public CartItemQuantityUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}

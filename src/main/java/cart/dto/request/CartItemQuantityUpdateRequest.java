package cart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CartItemQuantityUpdateRequest {

    private final Integer quantity;

    private CartItemQuantityUpdateRequest() {
        this.quantity = null;
    }
}

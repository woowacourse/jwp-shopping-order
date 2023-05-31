package cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CartItemRequest {

    private final Long productId;

    private CartItemRequest() {
        this.productId = null;
    }
}

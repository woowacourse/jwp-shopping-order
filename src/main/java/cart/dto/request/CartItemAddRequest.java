package cart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CartItemAddRequest {

    private final Long productId;

    private CartItemAddRequest() {
        this.productId = null;
    }
}

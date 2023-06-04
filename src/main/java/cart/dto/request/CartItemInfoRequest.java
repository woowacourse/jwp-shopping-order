package cart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CartItemInfoRequest {

    private final Long cartItemId;
    private final Integer quantity;
    private final ProductInfoRequest product;

    private CartItemInfoRequest() {
        this(null, null, null);
    }
}

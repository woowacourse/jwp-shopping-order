package cart.ui.dto.cartitem;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CartItemIdsRequest {

    @NotEmpty
    private List<Long> cartItemIds;

    public CartItemIdsRequest() {
    }

    public CartItemIdsRequest(final List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}

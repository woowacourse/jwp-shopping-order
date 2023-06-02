package cart.ui.dto.cartitem;

import java.util.List;

public class CartItemIdsRequest {

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

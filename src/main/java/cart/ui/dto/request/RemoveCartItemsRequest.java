package cart.ui.dto.request;

import java.util.List;

public class RemoveCartItemsRequest {

    private List<Long> cartItemIds;

    public RemoveCartItemsRequest() {
    }

    public RemoveCartItemsRequest(final List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}

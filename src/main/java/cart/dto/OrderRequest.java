package cart.dto;

import java.util.List;

public final class OrderRequest {
    private List<Long> cartItemIds;

    public OrderRequest() {
    }

    public OrderRequest(final List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }
}

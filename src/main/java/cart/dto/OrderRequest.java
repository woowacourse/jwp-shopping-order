package cart.dto;

import java.util.List;

public class OrderRequest {

    private List<Long> cartItemIds;

    public OrderRequest() {
    }

    public OrderRequest(List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

}

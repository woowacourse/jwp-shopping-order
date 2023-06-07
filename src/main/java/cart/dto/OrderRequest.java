package cart.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderRequest {
    private final List<Long> cartItemIds;

    @JsonCreator
    public OrderRequest(List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}

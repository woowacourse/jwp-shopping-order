package cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OrderRequest {

    @JsonProperty("cartItemList")
    private List<Long> cartItemIds;

    public OrderRequest() {
    }

    public OrderRequest(final List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}

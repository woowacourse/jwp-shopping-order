package cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CartItemDeleteRequest {

    @JsonProperty("cartItemIdList")
    private List<Long> cartItemIds;

    public CartItemDeleteRequest() {
    }

    public CartItemDeleteRequest(final List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}

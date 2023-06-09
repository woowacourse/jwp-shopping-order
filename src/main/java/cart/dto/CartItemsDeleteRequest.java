package cart.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CartItemsDeleteRequest {

    private final List<Long> cartItemIds;

    @JsonCreator
    public CartItemsDeleteRequest(@JsonProperty("cartItemIds") List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}

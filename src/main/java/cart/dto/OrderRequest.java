package cart.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

public class OrderRequest {

    private List<Long> cartItemIds;

    public OrderRequest() {
    }

    @JsonCreator
    public OrderRequest(List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

}

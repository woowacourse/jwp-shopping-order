package cart.dto.request;

import java.util.List;
import javax.validation.constraints.Size;

public class OrderRequest {

    @Size(min = 1)
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

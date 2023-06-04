package cart.dto.request;

import java.util.List;
import javax.validation.constraints.Size;

public class OrderRequest {

    @Size(min = 1)
    private List<Long> cartItemIds;
    private int usePoint;

    public OrderRequest() {
    }

    public OrderRequest(final List<Long> cartItemIds, final int usePoint) {
        this.cartItemIds = cartItemIds;
        this.usePoint = usePoint;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public int getUsePoint() {
        return usePoint;
    }
}

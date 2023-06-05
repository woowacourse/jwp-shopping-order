package cart.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

public class OrderRequest {
    @Size(min = 1)
    private final List<Long> cartItemIds;

    @Min(0)
    private final int usePoint;

    public OrderRequest(List<Long> cartItemIds, int usePoint) {
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

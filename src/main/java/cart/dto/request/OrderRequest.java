package cart.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class OrderRequest {
    @NotBlank
    @Size(min = 1)
    private final List<Long> cartItemIds;

    @NotBlank
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

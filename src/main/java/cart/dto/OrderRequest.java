package cart.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class OrderRequest {

    @NotEmpty
    private final List<Long> cartItemIds;
    @PositiveOrZero
    private final Integer point;

    public OrderRequest(final List<Long> cartItemIds, final Integer point) {
        this.cartItemIds = cartItemIds;
        this.point = point;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public Integer getPoint() {
        return point;
    }
}

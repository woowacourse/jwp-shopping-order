package cart.dto.request;

import java.util.List;

public class OrderRequest {
    private final List<Long> cartItemIds;
    private final Integer points;

    public OrderRequest(final List<Long> cartItemIds, final Integer points) {
        this.cartItemIds = cartItemIds;
        this.points = points;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public Integer getPoints() {
        return points;
    }
}

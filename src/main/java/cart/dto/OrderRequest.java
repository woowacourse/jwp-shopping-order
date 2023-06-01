package cart.dto;

import java.util.List;

public class OrderRequest {
    private final List<Long> cartIds;
    private final Integer point;

    public OrderRequest(List<Long> cartIds, Integer point) {
        this.cartIds = cartIds;
        this.point = point;
    }

    public List<Long> getCartIds() {
        return cartIds;
    }

    public Integer getPoint() {
        return point;
    }
}

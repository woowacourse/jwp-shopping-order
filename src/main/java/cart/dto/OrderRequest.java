package cart.dto;

import java.util.List;

public class OrderRequest {
    private final List<Integer> cartIds;
    private final Integer point;

    public OrderRequest(List<Integer> cartIds, Integer point) {
        this.cartIds = cartIds;
        this.point = point;
    }

    public List<Integer> getCartIds() {
        return cartIds;
    }

    public Integer getPoint() {
        return point;
    }
}

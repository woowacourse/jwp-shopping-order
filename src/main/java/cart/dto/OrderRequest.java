package cart.dto;

import java.util.List;

public class OrderRequest {

    private final List<Long> cartIds;
    private final int point;
    private final int totalPrice;

    public OrderRequest(final List<Long> cartIds, final int point, final int totalPrice) {
        this.cartIds = cartIds;
        this.point = point;
        this.totalPrice = totalPrice;
    }

    public List<Long> getCartIds() {
        return cartIds;
    }

    public int getPoint() {
        return point;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}

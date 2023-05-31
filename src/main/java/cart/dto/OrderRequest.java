package cart.dto;

import java.util.List;

public final class OrderRequest {
    private List<Long> cartIds;
    private int point;
    private int totalPrice;


    public OrderRequest() {
    }

    public OrderRequest(List<Long> cartIds, int point, int totalPrice) {
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

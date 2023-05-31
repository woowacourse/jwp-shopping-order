package cart.dto;

import java.util.List;

public class OrderRequest {

    private int usedPoints;
    private List<Long> cartItemIds;

    public OrderRequest() {

    }

    public OrderRequest(final int usedPoints, final List<Long> cartItemIds) {
        this.usedPoints = usedPoints;
        this.cartItemIds = cartItemIds;
    }

    public int getUsedPoints() {
        return usedPoints;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}

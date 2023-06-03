package cart.dto;

import java.util.List;

public class OrderRequest {

    private List<Long> cartItemIds;
    private int usePoint;

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

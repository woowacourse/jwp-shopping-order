package cart.dto.order;

import java.util.List;

public class OrderRequest {
    private final List<Long> cartItemIds;
    private final int finalPrice;

    public OrderRequest(final List<Long> cartItemIds, final int finalPrice) {
        this.cartItemIds = cartItemIds;
        this.finalPrice = finalPrice;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public int getFinalPrice() {
        return finalPrice;
    }
}

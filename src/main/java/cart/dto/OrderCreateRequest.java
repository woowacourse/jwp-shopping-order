package cart.dto;

import java.util.List;

public class OrderCreateRequest {

    private final List<Long> cartItemIds;
    private final int finalPrice;

    public OrderCreateRequest(final List<Long> cartItemIds, final int finalPrice) {
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

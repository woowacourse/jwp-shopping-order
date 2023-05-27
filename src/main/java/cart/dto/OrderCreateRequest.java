package cart.dto;

import java.util.List;

public class OrderCreateRequest {

    private final List<Long> cartItemIds;
    private final int totalPrice;

    public OrderCreateRequest(final List<Long> cartItemIds, final int totalPrice) {
        this.cartItemIds = cartItemIds;
        this.totalPrice = totalPrice;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

}

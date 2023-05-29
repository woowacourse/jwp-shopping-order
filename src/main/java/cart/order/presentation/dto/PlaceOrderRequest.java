package cart.order.presentation.dto;

import java.util.List;

public class PlaceOrderRequest {

    private List<Long> cartItemIds;

    public PlaceOrderRequest() {
    }

    public PlaceOrderRequest(List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}

package cart.order.presentation.dto;

import java.util.List;

public class PlaceOrderRequest {

    private List<Long> cartItemIds;
    private List<Long> couponIds;

    public PlaceOrderRequest() {
    }

    public PlaceOrderRequest(List<Long> cartItemIds, List<Long> couponIds) {
        this.cartItemIds = cartItemIds;
        this.couponIds = couponIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }
}

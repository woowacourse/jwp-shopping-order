package cart.dto;

import java.util.List;

public class OrderRequest {
    private final List<CartItemDto> cartItems;
    private final List<Long> couponIds;
    private final int deliveryFee;

    public OrderRequest(List<CartItemDto> cartItems, List<Long> couponIds, int deliveryFee) {
        this.cartItems = cartItems;
        this.couponIds = couponIds;
        this.deliveryFee = deliveryFee;
    }

    public List<CartItemDto> getCartItems() {
        return cartItems;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}

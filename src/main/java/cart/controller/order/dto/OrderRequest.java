package cart.controller.order.dto;

import java.util.List;

public class OrderRequest {
    private List<Long> cartItemIds;
    private boolean isDeliveryFree;
    private int totalPaymentPrice;
    private List<Long> couponIds;

    public OrderRequest() {
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public boolean isDeliveryFree() {
        return isDeliveryFree;
    }

    public int getTotalPaymentPrice() {
        return totalPaymentPrice;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public boolean isCartItemOrdered(Long id) {
        return this.cartItemIds.contains(id);
    }
}

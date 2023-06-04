package cart.domain.coupon;

import java.util.List;

public class paymentsRequest {
    private final List<Long> cartItemIds;
    private final List<Long> couponIds;
    private final boolean isDeliveryFree;
    private final int totalPaymentPrice;

    public paymentsRequest(List<Long> cartItemIds, List<Long> couponIds, boolean isDeliveryFree, int totalPaymentPrice) {
        this.cartItemIds = cartItemIds;
        this.couponIds = couponIds;
        this.isDeliveryFree = isDeliveryFree;
        this.totalPaymentPrice = totalPaymentPrice;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public boolean isDeliveryFree() {
        return isDeliveryFree;
    }

    public int getTotalPaymentPrice() {
        return totalPaymentPrice;
    }
}

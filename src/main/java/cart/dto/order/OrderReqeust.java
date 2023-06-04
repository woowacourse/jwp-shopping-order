package cart.dto.order;

import java.util.List;

public class OrderReqeust {
    private final List<Long> cartItemIds;
    private final List<Long> couponIds;
    private final int totalPaymentPrice;
    private final boolean isDeliveryFree;

    public OrderReqeust(List<Long> cartItemIds, List<Long> couponIds, int totalPaymentPrice, boolean isDeliveryFree) {
        this.cartItemIds = cartItemIds;
        this.couponIds = couponIds;
        this.totalPaymentPrice = totalPaymentPrice;
        this.isDeliveryFree = isDeliveryFree;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public int getTotalPaymentPrice() {
        return totalPaymentPrice;
    }

    public boolean isDeliveryFree() {
        return isDeliveryFree;
    }
}

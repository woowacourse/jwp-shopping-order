package cart.dto;

import java.util.List;

public class OrderRequest {
    private final List<Long> cartItemIds;
    private final int totalPrice;
    private final Long couponId;

    public OrderRequest(List<Long> cartItemIds, int price, Long couponId) {
        this.cartItemIds = cartItemIds;
        this.totalPrice = price;
        this.couponId = couponId;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public Long getCouponId() {
        return couponId;
    }

    public boolean isCouponNull() {
        return this.couponId == null;
    }
}

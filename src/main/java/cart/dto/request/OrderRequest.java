package cart.dto.request;

import java.util.List;

public class OrderRequest {
    private List<Long> cartItemIds;
    private Long couponId;

    public OrderRequest() {
    }

    public OrderRequest(List<Long> cartItemIds, Long couponId) {
        this.cartItemIds = cartItemIds;
        this.couponId = couponId;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public Long getCouponId() {
        return couponId;
    }
}
